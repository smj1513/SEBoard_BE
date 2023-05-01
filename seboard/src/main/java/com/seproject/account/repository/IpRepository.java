package com.seproject.account.repository;

import com.seproject.account.model.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends JpaRepository<Ip,Long> {
    Ip findByIpAddress(String ipAddress);
    boolean existsByIpAddress(String ipAddress);

}
