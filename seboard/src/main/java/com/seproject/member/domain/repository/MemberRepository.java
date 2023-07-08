package com.seproject.member.domain.repository;

import com.seproject.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("select m from Member m where m.account.loginId = :loginId")
    Optional<Member> findByLoginId(String loginId);
    @Query("select m from Member m where m.account.accountId = :accountId")
    Optional<Member> findByAccountId(Long accountId);
}