package com.seproject.admin.post.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.service.AccountService;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.post.controller.dto.AdminOldPost;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.model.Reply;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.comment.domain.repository.ReplyRepository;
import com.seproject.board.common.BaseTime;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.repository.CategoryRepository;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeOption;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAccessDeniedException;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.member.domain.Anonymous;
import com.seproject.member.domain.repository.AnonymousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPostSyncAppService {
    private final AccountService accountService;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final AnonymousRepository anonymousRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    @Transactional
    public String addOldPost(AdminOldPost request) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN, null));

        if(!account.getLoginId().equals("system")){
            throw new CustomAccessDeniedException(ErrorCode.ACCESS_DENIED, null);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        Anonymous anonymous = Anonymous.builder()
                .name(request.getAuthor())
                .account(account)
                .build();

        anonymousRepository.save(anonymous);

        Post post = Post.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .views(request.getViews())
                .baseTime(BaseTime.of(request.getCreatedAt(), request.getUpdatedAt()))
                .category(category)
                .author(anonymous)
                .exposeOption(
                        ExposeOption.of(ExposeState.PUBLIC, "")
                )
                .build();

        postRepository.save(post);

        for(AdminOldPost.OldCommentRequest ocq : request.getComments()){
            Anonymous commentAnonymous = Anonymous.builder()
                    .name(ocq.getAuthor())
                    .account(account)
                    .build();

            anonymousRepository.save(commentAnonymous);

            Comment comment = Comment.builder()
                    .contents(ocq.getContents())
                    .baseTime(BaseTime.of(ocq.getCreatedAt(), ocq.getUpdatedAt()))
                    .post(post)
                    .author(commentAnonymous)
                    .isOnlyReadByAuthor(false)
                    .build();

            commentRepository.save(comment);

            for(AdminOldPost.OldReplyRequest orq : ocq.getReplies()){
                Anonymous replyAnonymous = Anonymous.builder()
                        .name(orq.getAuthor())
                        .account(account)
                        .build();

                anonymousRepository.save(replyAnonymous);

                Reply reply = Reply.builder()
                        .superComment(comment)
                        .tag(comment)
                        .contents(orq.getContents())
                        .baseTime(BaseTime.of(orq.getCreatedAt(), orq.getUpdatedAt()))
                        .post(post)
                        .author(replyAnonymous)
                        .isOnlyReadByAuthor(false)
                        .build();

                replyRepository.save(reply);
            }
        }

        return "성공";
    }
}
