package com.seproject.seboard.domain.repository.user;

import com.seproject.seboard.domain.model.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query(value = "select * from members where account_id = :accountId", nativeQuery = true)
    Member findByAccountId(Long accountId);
}
