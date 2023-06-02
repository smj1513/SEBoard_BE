package com.seproject.admin.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.admin.controller.dto.file.FileRequest;
import com.seproject.admin.controller.dto.file.FileRequest.AdminFileRetrieveCondition;
import com.seproject.admin.controller.dto.file.FileResponse;
import com.seproject.admin.controller.dto.file.FileSearchOption;
import com.seproject.admin.domain.repository.AdminFileMetaDataSearchRepository;
import com.seproject.seboard.domain.model.common.QFileMetaData;
import com.seproject.seboard.domain.model.post.QPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.seproject.admin.controller.dto.file.FileResponse.*;
import static com.seproject.seboard.domain.model.common.QFileMetaData.fileMetaData;
import static com.seproject.seboard.domain.model.post.QPost.post;

@Repository
public class AdminFileMetaDataJpaSearchRepository implements AdminFileMetaDataSearchRepository {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public AdminFileMetaDataJpaSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminFileRetrieveResponse> findFileMetaDataByCondition(AdminFileRetrieveCondition condition, Pageable pageable) {
        List<AdminFileRetrieveResponse> contents = queryFactory
                .select(Projections.constructor(AdminFileRetrieveResponse.class,
                        fileMetaData))
                .from(fileMetaData)
                .leftJoin(fileMetaData.post, post)
                .where(
                        orphanEq(condition.getIsOrphan()),
                        searchOption(condition.getSearchOption(), condition.getQuery())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(fileMetaData.baseTime.createdAt.desc())
                .fetch();

        Long count = queryFactory
                .select(fileMetaData.count())
                .from(fileMetaData)
                .leftJoin(fileMetaData.post, post)
                .where(
                        orphanEq(condition.getIsOrphan()),
                        searchOption(condition.getSearchOption(), condition.getQuery())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, count);
    }

    private BooleanExpression searchOption(String searchOption, String query){
        if(searchOption==null || query==null){
            return null;
        }

        switch (FileSearchOption.valueOf(searchOption)){
            case AUTHOR:{
                return post.author.name.contains(query);
            }
            case TITLE:{
                return fileMetaData.originalFileName.contains(query);
            }
            default:
                return null;
        }
    }

    private BooleanExpression orphanEq(Boolean isOrphan){
        if(isOrphan==null){
            return null;
        }else if(isOrphan) {
            return fileMetaData.post.isNull();
        }else{
            return fileMetaData.post.isNotNull();
        }
    }
}
