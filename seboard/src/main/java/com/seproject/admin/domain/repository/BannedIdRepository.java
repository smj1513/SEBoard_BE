package com.seproject.admin.domain.repository;

import com.seproject.admin.domain.BannedId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedIdRepository extends JpaRepository<BannedId,Long> {

    boolean existsByBannedId(String bannedId);

    Optional<BannedId> findByBannedId(String bannedId);
}
