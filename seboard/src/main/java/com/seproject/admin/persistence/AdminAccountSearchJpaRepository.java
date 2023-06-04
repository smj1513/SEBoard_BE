package com.seproject.admin.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.admin.domain.repository.AdminAccountSearchRepository;
import com.seproject.admin.dto.AccountDTO;
import com.seproject.admin.dto.AccountDTO.AdminRetrieveAccountCondition;
import com.seproject.admin.dto.AccountDTO.RetrieveAccountResponse;
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.createdAt.desc())
                .fetch();


        Long count = queryFactory
                .select(account.count())
                .from(account)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
