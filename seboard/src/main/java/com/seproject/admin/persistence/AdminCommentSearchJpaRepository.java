package com.seproject.admin.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.admin.controller.dto.comment.AdminCommentRequest;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse;
import com.seproject.admin.domain.repository.AdminCommentSearchRepository;
import com.seproject.seboard.controller.PostSearchOptions;
import com.seproject.seboard.domain.model.comment.QComment;
import com.seproject.seboard.domain.model.common.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.seproject.admin.controller.dto.comment.AdminCommentRequest.*;
import static com.seproject.admin.controller.dto.comment.AdminCommentResponse.*;
import static com.seproject.seboard.domain.model.comment.QComment.comment;
import static com.seproject.seboard.domain.model.post.QPost.post;

@Repository
public class AdminCommentSearchJpaRepository implements AdminCommentSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public AdminCommentSearchJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminCommentListResponse> findCommentListByCondition(AdminCommentRetrieveCondition condition, Pageable pageable) {
        List<AdminCommentListResponse> contents = queryFactory
                .select(Projections.constructor(AdminCommentListResponse.class,
                        comment))
                .from(comment)
                .where(
                        searchOption(condition.getSearchOption(), condition.getQuery()),
                        reportedStatusEq(condition.getIsReported())
                ).orderBy(comment.baseTime.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        searchOption(condition.getSearchOption(), condition.getQuery()),
                        reportedStatusEq(condition.getIsReported())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, count);
    }

    private BooleanExpression reportedStatusEq(Boolean isReported){
        if(isReported == null){
            return comment.status.eq(Status.NORMAL)
                    .or(comment.status.eq(Status.REPORTED));
        }else if(!isReported){
            return comment.status.eq(Status.NORMAL);
        } else{
            return comment.status.eq(Status.REPORTED);
        }
    }

    private BooleanExpression searchOption(String searchOption, String query){
        if(searchOption==null || query==null){
            return null;
        }

        BooleanExpression contentOption = comment.contents.contains(query);
        BooleanExpression authorOption = comment.author.name.contains(query);

        switch (PostSearchOptions.valueOf(searchOption)){
            case CONTENT:{
                return contentOption;
            }
            case AUTHOR:{
                return authorOption;
            }
            case ALL:{
                return contentOption
                        .or(authorOption);
            }
            default:{
                return null;
            }
        }
    }
}
