package com.seproject.account.service;

import com.seproject.account.model.Ip;
import com.seproject.account.repository.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.seproject.admin.dto.IpDTO.*;

@RequiredArgsConstructor
@Service
public class IpService {
    private final IpRepository ipRepository;

    public List<Ip> findAll() {
        return ipRepository.findAll();
    }

    public Ip banIp(CreateIpRequest createIpRequest) {
        Ip ip = Ip.builder()
                .ipAddress(createIpRequest.getIpAddress())
                .build();

        ipRepository.save(ip);
        return ip;
    }

    public Ip unBanIp(DeleteIpRequest deleteIpRequest) {

        Ip ip = ipRepository.findByIpAddress(deleteIpRequest.getIpAddress());
        if(ip == null) throw new NoSuchElementException();
        ipRepository.delete(ip);
        return ip;
    }

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
