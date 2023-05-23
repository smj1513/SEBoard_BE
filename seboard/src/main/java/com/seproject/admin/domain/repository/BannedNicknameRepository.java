package com.seproject.admin.domain.repository;

import com.seproject.admin.domain.BannedNickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedNicknameRepository extends JpaRepository<BannedNickname,Long> {

    boolean existsByBannedNickname(String bannedNickname);

    Optional<BannedNickname> findByBannedNickname(String bannedNickname);
}
