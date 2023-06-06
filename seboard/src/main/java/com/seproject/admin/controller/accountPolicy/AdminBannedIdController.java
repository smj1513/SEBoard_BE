package com.seproject.admin.controller.accountPolicy;

import com.seproject.admin.service.BannedIdService;
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

import static com.seproject.admin.dto.BannedIdDTO.*;

@Tag(name = "금지 아이디 관리 API", description = "관리자 시스템의 금지 아이디 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin/accountPolicy/bannedId")
@RestController
public class AdminBannedIdController {

    private final BannedIdService bannedIdService;

    @Operation(summary = "금지 아이디 목록 조회", description = "금지 아이디를 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllBannedIdResponse.class)), responseCode = "200", description = "금지 아이디 목록 조회 성공"),
    })
    @GetMapping()
    public ResponseEntity<?> retrieveAllBannedId() {
        RetrieveAllBannedIdResponse all = bannedIdService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Operation(summary = "금지 아이디 추가", description = "금지 아이디를 추가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금지 아이디 추가 성공"),
    })
    @PostMapping()
    public ResponseEntity<?> createBannedId(@RequestBody CreateBannedIdRequest createBannedIdRequest) {
        bannedIdService.banId(createBannedIdRequest.getBannedId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "금지 아이디 삭제", description = "금지 아이디를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금지 아이디 삭제 성공"),
    })
    @DeleteMapping()
    public ResponseEntity<?> deleteBannedId(@RequestBody DeleteBannedIdRequest deleteBannedIdRequest) {
        bannedIdService.unbanId(deleteBannedIdRequest.getBannedId());
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
