package com.seproject.seboard.domain.repository.user;

import com.seproject.seboard.domain.model.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
