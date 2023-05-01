package com.seproject.admin.controller;

import com.seproject.admin.dto.BannedIdDTO;
import com.seproject.admin.service.BannedNicknameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.seproject.admin.dto.BannedNicknameDTO.*;

@Tag(name = "금지 닉네임 관리 API", description = "관리자 시스템의 금지 닉네임 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminBannedNicknameController {

    private final BannedNicknameService bannedNicknameService;

    @Operation(summary = "금지 닉네임 목록 조회", description = "금지 닉네임을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllBannedNicknameResponse.class)), responseCode = "200", description = "금지 닉네임 목록 조회 성공"),
    })
    @GetMapping("/bannedNickname")
    public ResponseEntity<?> retrieveAllBannedNickname() {
        RetrieveAllBannedNicknameResponse all = bannedNicknameService.findAll();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @Operation(summary = "금지 닉네임 추가", description = "금지 닉네임을 추가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금지 닉네임 추가 성공"),
    })
    @PostMapping("/bannedNickname")
    public ResponseEntity<?> createBannedNickname(@RequestBody CreateBannedNicknameRequest createBannedNicknameRequest) {
        bannedNicknameService.banNickname(createBannedNicknameRequest.getNickname());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "금지 닉네임 삭제", description = "금지 닉네임을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금지 닉네임 삭제 성공"),
    })
    @DeleteMapping("/bannedNickname")
    public ResponseEntity<?> deleteBannedNickname(@RequestBody DeleteBannedNicknameRequest deleteBannedNicknameRequest) {
        bannedNicknameService.unbanNickname(deleteBannedNicknameRequest.getNickname());
        return new ResponseEntity<>(HttpStatus.OK);
    }











}
