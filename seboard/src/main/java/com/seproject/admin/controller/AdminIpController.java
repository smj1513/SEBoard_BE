package com.seproject.admin.controller;

import com.seproject.account.model.Ip;
import com.seproject.account.service.IpService;
import com.seproject.admin.dto.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.seproject.admin.dto.IpDTO.*;

@Tag(name = "아이피 관리 API", description = "관리자 시스템이 갖는 금지 아이피 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@RestController
public class AdminIpController {

    private final IpService ipService;

    @Operation(summary = "금지 아이피 목록 조회", description = "접근이 금지된 아이피의 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllIpResponse.class)), responseCode = "200", description = "금지 아피피 목록 조회 성공"),
    })
    @GetMapping("/ip")
    public ResponseEntity<?> retrieveAllBannedIp() {
        List<Ip> all = ipService.findAll();
        return new ResponseEntity<>(RetrieveAllIpResponse.toDTO(all), HttpStatus.OK);
    }


    @Operation(summary = "금지 아이피 추가", description = "접근을 금지할 아이피를 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateIpResponse.class)), responseCode = "200", description = "금지 아이피 등록 조회 성공")
    })
    @PostMapping("/ip")
    public ResponseEntity<?> createBannedIp(@RequestBody CreateIpRequest createIpRequest) {
        Ip ip = ipService.banIp(createIpRequest);
        return new ResponseEntity<>(CreateIpResponse.toDTO(ip),HttpStatus.OK);
    }

    @Operation(summary = "금지 아이피 해제", description = "접근이 금지된 아이피를 삭제(접근 허용)한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteIpRequest.class)), responseCode = "200", description = "금지 아이피 삭제 성공")
    })
    @DeleteMapping("/ip")
    public ResponseEntity<?> deleteBannedIp(@RequestBody DeleteIpRequest deleteIpRequest) {
        Ip ip = ipService.unBanIp(deleteIpRequest);
        return new ResponseEntity<>(DeleteIpResponse.toDTO(ip),HttpStatus.OK);
    }
}
