package com.seproject.admin.dto;

import com.seproject.account.Ip.domain.Ip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class IpDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveIpResponse {
        private Long id;
        private String ipAddress;

        public static RetrieveIpResponse toDTO(Ip ip) {
            return builder()
                    .id(ip.getId())
                    .ipAddress(ip.getIpAddress())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAllIpResponse {
        private List<RetrieveIpResponse> ips;
        public static RetrieveAllIpResponse toDTO(List<Ip> ip) {
            return builder()
                    .ips(ip.stream()
                            .map(RetrieveIpResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    public static class CreateIpRequest {
        private String ipAddress;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class CreateIpResponse {
        private Long id;
        private String ipAddress;

        public static CreateIpResponse toDTO(Ip ip) {
            return builder()
                    .id(ip.getId())
                    .ipAddress(ip.getIpAddress())
                    .build();
        }
    }

    @Data
    public static class DeleteIpRequest {
        private String ipAddress;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class DeleteIpResponse {
        private Long id;
        private String ipAddress;

        public static DeleteIpResponse toDTO(Ip ip) {
            return builder()
                    .id(ip.getId())
                    .ipAddress(ip.getIpAddress())
                    .build();
        }
    }
}
