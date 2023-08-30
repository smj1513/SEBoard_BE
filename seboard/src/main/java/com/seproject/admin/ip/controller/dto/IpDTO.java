package com.seproject.admin.ip.controller.dto;

import com.seproject.account.Ip.domain.Ip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

public class IpDTO {

    @Data
    public static class IpResponse {
        private Long id;
        private String ipAddress;

        public IpResponse(Long id, String ipAddress) {
            this.id = id;
            this.ipAddress = ipAddress;
        }
    }

    @Data
    public static class CreateIpRequest {
        private String ipAddress;

    }
    @Data
    public static class DeleteIpRequest {
        private String ipAddress;

    }

    @Data
    public static class IpCondition {

    }

}
