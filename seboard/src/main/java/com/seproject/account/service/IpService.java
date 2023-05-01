package com.seproject.account.service;

import com.seproject.account.model.Ip;
import com.seproject.account.repository.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IpService {
    private final IpRepository ipRepository;

    public boolean existIpAddress(String ipAddress) {
        return ipRepository.existsByIpAddress(ipAddress);
    }

    public Ip findIpByAddress(String address) {
        return ipRepository.findByIpAddress(address);
    }

    public void addIp(String address) {
        Ip ip = Ip.builder()
                .ipAddress(address)
                .build();

        ipRepository.save(ip);
    }
}
