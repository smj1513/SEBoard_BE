package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.post.PostCommand.PostEditCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostListFindCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.CategoryRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.seproject.seboard.application.utils.AppServiceHelper.*;
import static com.seproject.seboard.controller.dto.post.PostResponse.*;

@Service
@RequiredArgsConstructor
public class PostAppService {
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final CategoryRepository categoryRepository;
    private final AnonymousRepository anonymousRepository;
    private final MemberRepository memberRepository;
    private final FileMetaDataRepository fileMetaDataRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FileAppService fileAppService;

    public void writePost(PostWriteCommand command){
        if(command.isAnonymous()){
            writeUnnamedPost(command);
        }else{
            writeNamedPost(command);
        }
    }

    @Transactional
    protected void writeUnnamedPost(PostWriteCommand command) { //accID는 체킹되었다고 가정
        Anonymous anonymous = Anonymous.builder()
                .name("익명") //TODO : 익명 이름 다양하게?
                .accountId(command.getAccountId())
                .build();

        anonymousRepository.save(anonymous);

        createPost(command, anonymous);
    }

    protected void writeNamedPost(PostWriteCommand command) {
        Member member = memberRepository.findByAccountId(command.getAccountId()).orElseThrow(NoSuchElementException::new);

        if (member == null) {
            //TODO : member 생성 로직 호출
        }

        createPost(command, member);
    }
    private void createPost(PostWriteCommand command, BoardUser author){
        Category category = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");

        List<FileMetaData> fileMetaDataList =
                fileMetaDataRepository.findAllById(command.getAttachmentIds());

        Post post = Post.builder()
                .title(command.getTitle())
                .contents(command.getContents())
                .category(category)
                .author(author)
                .baseTime(BaseTime.now())
                .pined(command.isPined())
                .attachments(new HashSet<>(fileMetaDataList))
                //TODO : 좀더 유연하게 변경?
                .exposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()))
                .build();

        postRepository.save(post);
    }


    //TODO : paging 처리해야함
    public RetrievePostListResponse findPostList(PostListFindCommand command, boolean pinedOption){
//        Page<Post> postPage = null;
//
//        if(pinedOption){
//            postPage = postSearchRepository.findPinedPostByCategoryId(command.getCategoryId(), PageRequest.of(command.getPage(), command.getSize()));
//        }else{
//            postPage = postSearchRepository.findByCategoryId(command.getCategoryId(), PageRequest.of(command.getPage(), command.getSize())));
//        }
//
//        List<RetrievePostListResponseElement> postDtoList = postPage .stream()
//            .map(post -> {
//                int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
//                return RetrievePostListResponseElement.toDTO(post, commentSize);
//            }).collect(Collectors.toList());
//
//        PaginationResponse paginationResponse = PaginationResponse.builder()
//                .currentPage(postPage.getCurPage())
//                .contentSize(postPage.getTotalSize())
//                .perPage(postPage.getPerPage())
//                .lastPage(postPage.getLastPage())
//                .build();
//
//        return RetrievePostListResponse.toDTO(postDtoList, paginationResponse);
        return null;
    }

    @Transactional
    public void editPost(PostEditCommand command) {
        Post post = findByIdOrThrow(command.getPostId(), postRepository, "");

        //TODO : 추후 활성화 필요
//        if(!post.isWrittenBy(command.getAccountId())){
//            throw new IllegalArgumentException();
//        }

        post.changeTitle(command.getTitle());
        post.changeContents(command.getContents());
        post.changePin(command.isPined());
        post.changeExposeOption(command.getExposeState(), command.getPrivatePassword());

        //TODO : 좀더 깔끔하게 처리?
        List<FileMetaData> attachments =
                fileMetaDataRepository.findAllById(command.getAttachmentIds()); //요청으로 들어온 attachment PK

        Set<FileMetaData> removalAttachments = post.getAttachments();
        removalAttachments.removeAll(attachments); //요청으로 들어온 PK와 기존의 PK를 비교하고, 새로온 PK에 없는 것은 삭제 대상

        removalAttachments.forEach(fileAppService::deleteFileFromStorage); //file 삭제
        removalAttachments.forEach(fileMetaData -> post.removeAttachment(fileMetaData)); //db에서 정보 삭제

        attachments.forEach(fileMetaData -> post.addAttachment(fileMetaData));

        //TODO : category 권한 체킹 필요
        Category category = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");
        post.changeCategory(category);
    }

    public void removePost(Long postId, Long accountId) {
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : 추후 활성화 필요
//        if(!post.isWrittenBy(accountId)){ // TODO : 관리자 삭제 경우 추가해야함
//            throw new IllegalArgumentException();
//        }

        post.getAttachments().forEach(fileAppService::deleteFileFromStorage); //TODO : fileSystem에서 transactional 처리 필요
        postRepository.deleteById(postId);
    }
}
