package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.author.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
