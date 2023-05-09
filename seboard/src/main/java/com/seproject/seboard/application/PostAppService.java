package com.seproject.seboard.application;

import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.application.dto.post.PostCommand.PostEditCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.category.CategoryRepository;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
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


@Service
@RequiredArgsConstructor
@Transactional
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
    private final AccountRepository accountRepository;

    public Long writePost(PostWriteCommand command){
        Account account = accountRepository.findByLoginId(command.getLoginId());

        if(command.isAnonymous()){
            return writeUnnamedPost(command, account.getAccountId());
        }else{
            return writeNamedPost(command, account.getAccountId());
        }
    }

    protected Long writeUnnamedPost(PostWriteCommand command, Long accountId) {
        Anonymous anonymous = Anonymous.builder()
                .name("익명") //TODO : 익명 이름 다양하게?
                .accountId(accountId)
                .build();

        anonymousRepository.save(anonymous);

        return createPost(command, anonymous);
    }

    protected Long writeNamedPost(PostWriteCommand command, Long accountId) {
        Member member = memberRepository.findByAccountId(accountId).orElseThrow(NoSuchElementException::new);

        return createPost(command, member);
    }

    private Long createPost(PostWriteCommand command, BoardUser author){
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

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
                .exposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()))
                .build();

        postRepository.save(post);

        return post.getPostId();
    }



    public Long editPost(PostEditCommand command) {
        Account account = accountRepository.findByLoginId(command.getLoginId());

        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        if(!post.isWrittenBy(account.getAccountId())){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

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

        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));
        post.changeCategory(category);

        return post.getPostId();
    }

    public void removePost(Long postId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        if(!post.isWrittenBy(account.getAccountId())){ // TODO : 관리자 삭제 경우 추가해야함
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        post.delete();
//        post.getAttachments().forEach(fileAppService::deleteFileFromStorage); //TODO : fileSystem에서 transactional 처리 필요
//        postRepository.deleteById(postId);
    }
}
