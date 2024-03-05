package com.seproject.admin.ip.application;

import com.seproject.account.Ip.application.IpService;
import com.seproject.account.Ip.domain.IpType;
import com.seproject.admin.ip.persitence.IpQueryRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seproject.admin.ip.controller.dto.IpDTO.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AdminIpAppService {

    private final IpService ipService;
    private final IpQueryRepository ipQueryRepository;

    public List<IpResponse> findAll(IpCondition condition) {
//        PageRequest pageRequest = PageRequest.of(page, perPage);
        return ipQueryRepository.findAll(condition);

    }

    @Transactional
    public void createIp(CreateIpRequest request) {
        IpType ipType = IpType.valueOf(request.getIpType());
        ipService.createIp(request.getIpAddress(),ipType);
    }

    @Transactional
    public void delete(DeleteIpRequest request) {
        ipService.deleteIp(request.getIpAddress());
    }

}
