package com.seproject.admin.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.admin.domain.repository.AdminAccountSearchRepository;
import com.seproject.admin.dto.AccountDTO.AdminRetrieveAccountCondition;
import com.seproject.admin.dto.AccountDTO.RetrieveAccountResponse;
import com.seproject.board.common.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.seproject.account.model.account.QAccount.account;

@Repository
public class AdminAccountSearchJpaRepository implements AdminAccountSearchRepository {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public AdminAccountSearchJpaRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RetrieveAccountResponse> findAllAccount(AdminRetrieveAccountCondition condition, Pageable pageable) {
        List<RetrieveAccountResponse> content = queryFactory
                .select(Projections.constructor(RetrieveAccountResponse.class,
                        account))
                .from(account)
                .where(account.status.eq(Status.NORMAL))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.createdAt.desc())
                .fetch();


        Long count = queryFactory
                .select(account.count())
                .where(account.status.eq(Status.NORMAL))
                .from(account)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<RetrieveAccountResponse> findDeletedAccount(Pageable pageable) {
        List<RetrieveAccountResponse> content = queryFactory
                .select(Projections.constructor(RetrieveAccountResponse.class,
                        account))
                .from(account)
                .where(account.status.eq(Status.TEMP_DELETED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.createdAt.desc())
                .fetch();


        Long count = queryFactory
                .select(account.count())
                .from(account)
                .where(account.status.eq(Status.TEMP_DELETED))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
