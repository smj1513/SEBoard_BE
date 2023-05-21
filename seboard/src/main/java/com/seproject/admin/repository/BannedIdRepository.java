package com.seproject.admin.repository;

import com.seproject.admin.domain.BannedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BannedIdRepository extends JpaRepository<BannedId,Long> {

    @Query(nativeQuery = true, value = "select exists(select * from banned_id where banned_id = :bannedId)")
    boolean existsByBannedId(String bannedId);

    Optional<BannedId> findByBannedId(String bannedId);
}
