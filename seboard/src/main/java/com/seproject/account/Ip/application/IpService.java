package com.seproject.account.Ip.application;

import com.seproject.account.Ip.domain.Ip;
import com.seproject.account.Ip.domain.repository.IpRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IpService {
    private final IpRepository ipRepository;

    public List<Ip> findAll() {
        return ipRepository.findAll();
    }

    public Long createIp(String ipAddress) {
        Ip ip = Ip.builder()
                .ipAddress(ipAddress)
                .build();

        ipRepository.save(ip);
        return ip.getId();
    }

    public void deleteIp(String ipAddress) {
        Ip ip = ipRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_IP, null));
        ipRepository.delete(ip);
    }

    public boolean existIpAddress(String ipAddress) {
        return ipRepository.existsByIpAddress(ipAddress);
    }

//    public Ip findIpByAddress(String address) {
//        return ipRepository.findByIpAddress(address)
//                .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_IP,null));
//    }

}
