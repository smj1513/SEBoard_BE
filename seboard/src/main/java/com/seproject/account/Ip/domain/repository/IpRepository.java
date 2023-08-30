package com.seproject.account.Ip.domain.repository;

import com.seproject.account.Ip.domain.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpRepository extends JpaRepository<Ip,Long> {
    Optional<Ip> findByIpAddress(String ipAddress);
    boolean existsByIpAddress(String ipAddress);

}
