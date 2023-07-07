package com.seproject.board.post.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.board.post.controller.dto.AdminPostRequest.AdminPostRetrieveCondition;
import com.seproject.board.post.controller.dto.AdminPostResponse;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminPostRetrieveResponse;
import com.seproject.board.post.domain.repository.AdminPostSearchRepository;
import com.seproject.board.post.controller.PostSearchOptions;
import com.seproject.board.common.Status;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.seproject.board.domain.model.category.QMenu.menu;
import static com.seproject.board.domain.model.post.QPost.post;

@Repository
public class AdminPostSearchJpaRepository implements AdminPostSearchRepository {
    private JPAQueryFactory queryFactory;

    @Autowired
    public AdminPostSearchJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminPostRetrieveResponse> findPostListByCondition(AdminPostRetrieveCondition condition, Pageable pageable) {
        List<AdminPostRetrieveResponse> contents = queryFactory
                .select(Projections.constructor(
                        AdminPostRetrieveResponse.class,
                        post
                ))
                .from(post)
                .join(menu)
                .on(post.category.eq(menu))
                .where(
                        exposeOptionEq(condition.getExposeOption()),
                        categoryEq(condition.getCategoryId()),
                        reportedStatusEq(condition.getIsReported()),
                        searchOption(condition.getSearchOption(), condition.getQuery())
                )
                .orderBy(post.baseTime.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(post.count())
                .from(post)
                .join(menu)
                .on(post.category.eq(menu))
                .where(
                        exposeOptionEq(condition.getExposeOption()),
                        categoryEq(condition.getCategoryId()),
                        reportedStatusEq(condition.getIsReported()),
                        searchOption(condition.getSearchOption(), condition.getQuery())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, count);
    }

    @Override
    public Page<AdminPostResponse.AdminDeletedPostResponse> findDeletedPostList(Pageable pageable) {
        List<AdminPostResponse.AdminDeletedPostResponse> content = queryFactory
                .select(Projections.constructor(
                        AdminPostResponse.AdminDeletedPostResponse.class,
                        post
                ))
                .from(post)
                .where(post.status.eq(Status.TEMP_DELETED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.baseTime.createdAt.desc())
                .fetch();

        Long count = queryFactory
                .select(post.count())
                .from(post)
                .where(post.status.eq(Status.TEMP_DELETED))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression exposeOptionEq(String exposeOption){
        return exposeOption != null ? post.exposeOption.exposeState.eq(ExposeState.valueOf(exposeOption)) : null;
    }

    private BooleanExpression searchOption(String searchOption, String query){
        if(searchOption==null || query==null){
            return null;
        }

        BooleanExpression titleOption = post.title.contains(query);
        BooleanExpression contentOption = post.contents.contains(query);
        BooleanExpression authorOption = post.author.name.contains(query);

        switch (PostSearchOptions.valueOf(searchOption)){
            case TITLE:{
                return titleOption;
            }
            case CONTENT:{
                return contentOption;
            }
            case TITLE_OR_CONTENT:{
                return contentOption
                        .or(titleOption);
            }
            case AUTHOR:{
                return authorOption;
            }
            case ALL:{
                return titleOption
                        .or(contentOption)
                        .or(authorOption);
            }
            default:{
                return null;
            }
        }
    }

    private BooleanExpression categoryEq(Long categoryId){
        return categoryId != null ? menu.menuId.eq(categoryId).or(menu.superMenu.menuId.eq(categoryId)) : null;
    }

    private BooleanExpression reportedStatusEq(Boolean isReported){
        if(isReported == null){
            return post.status.eq(Status.NORMAL)
                    .or(post.status.eq(Status.REPORTED));
        }else if(!isReported){
            return post.status.eq(Status.NORMAL);
        } else{
            return post.status.eq(Status.REPORTED);
        }
    }
}
