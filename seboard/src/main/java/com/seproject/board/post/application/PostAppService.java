package com.seproject.board.post.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.repository.AccountRepository;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.file.domain.model.FileConfiguration;
import com.seproject.file.domain.repository.FileConfigurationRepository;
import com.seproject.file.application.FileAppService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomUserNotFoundException;
import com.seproject.error.exception.ExceedFileSizeException;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.post.application.dto.PostCommand.PostEditCommand;
import com.seproject.board.post.application.dto.PostCommand.PostWriteCommand;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.common.BaseTime;
import com.seproject.file.domain.model.FileMetaData;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeOption;
import com.seproject.member.domain.Anonymous;
import com.seproject.member.domain.BoardUser;
import com.seproject.member.domain.Member;
import com.seproject.board.menu.domain.repository.CategoryRepository;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.file.domain.repository.FileMetaDataRepository;
import com.seproject.file.domain.repository.FileRepository;
import com.seproject.board.post.domain.repository.BookmarkRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.board.post.domain.repository.PostSearchRepository;
import com.seproject.member.domain.repository.AnonymousRepository;
import com.seproject.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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
    private final FileRepository fileRepository;
    private final FileConfigurationRepository fileConfigurationRepository;

    public Long writePost(PostWriteCommand command){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_LOGIN));

        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        if(!category.editable(account.getRoles())){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        if(command.isAnonymous()){
            return writeUnnamedPost(command, account, category);
        }else{
            return writeNamedPost(command, account.getAccountId(), category);
        }
    }

    protected Long writeUnnamedPost(PostWriteCommand command, Account account, Category category) {
        Anonymous anonymous = Anonymous.builder()
                .name("익명") //TODO : 익명 이름 다양하게?
                .account(account)
                .build();

        anonymousRepository.save(anonymous);

        return createPost(command, anonymous, category);
    }

    protected Long writeNamedPost(PostWriteCommand command, Long accountId, Category category) {
        Member member = memberRepository.findByAccountId(accountId).orElseThrow(NoSuchElementException::new);

        return createPost(command, member, category);
    }

    private Long createPost(PostWriteCommand command, BoardUser author, Category category){
        List<FileMetaData> fileMetaDataList =
                fileMetaDataRepository.findAllById(command.getAttachmentIds());

        validFileListSize(fileMetaDataList);

        boolean isPined = command.isPined();

        if(!category.manageable(author.getAccount().getRoles())){
            isPined = false;
        }

        Post post = Post.builder()
                .title(command.getTitle())
                .contents(command.getContents())
                .category(category)
                .author(author)
                .baseTime(BaseTime.now())
                .pined(isPined)
                .attachments(new HashSet<>(fileMetaDataList))
                .exposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()))
                .build();

        postRepository.save(post);

        return post.getPostId();
    }



    public Long editPost(PostEditCommand command) {
        Account account = accountRepository.findByLoginId(command.getLoginId())
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        if(!post.isWrittenBy(account.getAccountId()) &&  !post.getCategory().manageable(account.getRoles())){
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

        removalAttachments.forEach(fileMetaData -> fileRepository.delete(fileMetaData.getFilePath())); //file 삭제
        removalAttachments.forEach(fileMetaData -> post.removeAttachment(fileMetaData)); //db에서 정보 삭제

        attachments.forEach(fileMetaData -> post.addAttachment(fileMetaData));

        validFileListSize(new ArrayList<>(post.getAttachments()));

        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));
        post.changeCategory(category);

        return post.getPostId();
    }

    private void validFileListSize(List<FileMetaData> fileMetaDataList){
        Long maxSize = fileConfigurationRepository.findAll().stream().findFirst()
                .orElseGet(() -> new FileConfiguration(100L, 100L)).getMaxSizePerPost();

        Long totalSize = fileMetaDataList.stream().mapToLong(fileMetaData -> fileMetaData.getFileSize()/(1024*1024)).sum();

        if(maxSize<totalSize){
            throw new ExceedFileSizeException(ErrorCode.INVALID_FILE_SIZE);
        }
    }

    public void removePost(Long postId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        if(!post.isWrittenBy(account.getAccountId()) &&  !post.getCategory().manageable(account.getRoles())){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        post.delete(true);
//        post.getAttachments().forEach(fileAppService::deleteFileFromStorage); //TODO : fileSystem에서 transactional 처리 필요
//        postRepository.deleteById(postId);
    }
}
