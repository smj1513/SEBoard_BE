package com.seproject.admin.controller;

import com.seproject.account.account.service.AccountService;
import com.seproject.board.common.controller.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.seproject.admin.dto.AccountDTO.*;

@Tag(name = "계정 관리 API", description = "관리자 시스템이 갖는 계정 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin/accounts")
@RestController
public class AdminAccountController {

    private final AccountService accountService;

    @Operation(summary = "등록된 계정 목록 조회", description = "계정 관리를 위하여 등록된 계정 목록을 확인한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllAccountResponse.class)), responseCode = "200", description = "계정 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 페이징 정보")
    })

    @GetMapping
    public ResponseEntity<Page<RetrieveAccountResponse>> retrieveAllAccount(@ModelAttribute AdminRetrieveAccountCondition condition,
                                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "perPage", defaultValue = "25") int perPage) {
        return ResponseEntity.ok(accountService.findAllAccount(condition, PageRequest.of(page, perPage)));
    }

    @GetMapping("/deleted")
    public ResponseEntity<Page<RetrieveAccountResponse>> retrieveDeletedAccount(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                @RequestParam(value = "perPage", defaultValue = "25") int perPage) {
        return ResponseEntity.ok(accountService.findDeletedAccount(PageRequest.of(page, perPage)));
    }


    @Operation(summary = "등록된 계정 상세 조회", description = "계정의 상세 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAccountResponse.class)), responseCode = "200", description = "계정 상세 정보 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존재하지 않는 계정")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<?> retrieveAllAccount(@PathVariable Long accountId) {
        try{
            return new ResponseEntity<>(accountService.findAccount(accountId), HttpStatus.OK);
        }  catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 유저를 찾을수 없습니다.",HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "계정 생성", description = "관리자는 계정을 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateAccountResponse.class)), responseCode = "200", description = "계정 생성 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이메일 형식이 일치하지 않음"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "존재하지 않는 권한 요청")
    })
    @PostMapping
    public ResponseEntity<?> createAccountByAdmin(@RequestBody CreateAccountRequest createAccountRequest) {
           CreateAccountResponse account = accountService.createAccount(createAccountRequest);
           return new ResponseEntity<>(account,HttpStatus.OK);
    }

    @Operation(summary = "계정 수정", description = "관리자는 계정을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateAccountResponse.class)), responseCode = "200", description = "계정 수정 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이메일 형식이 일치하지 않음"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "존재하지 않는 권한 요청"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존재하지 않는 계정")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@RequestBody UpdateAccountRequest updateAccountRequest, @PathVariable Long accountId) {

        UpdateAccountResponse updateAccount = accountService.updateAccount(updateAccountRequest, accountId);
        return new ResponseEntity<>(updateAccount,HttpStatus.OK);

    }

    @Operation(summary = "계정 삭제", description = "관리자는 계정을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteAccountResponse.class)), responseCode = "200", description = "계정 삭제 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존자해지 않는 계정")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(Long accountId) {

        try{
            DeleteAccountResponse deleteAccount = accountService.deleteAccount(accountId);
            return new ResponseEntity<>(deleteAccount,HttpStatus.OK);
        }  catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 계정을 찾을수 없습니다.",HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteBulkAccount(@RequestBody AdminBulkAccountRequest request){
        accountService.deleteBulkAccount(request.getAccountIds(), false);
        return ResponseEntity.ok(MessageResponse.of("계정 삭제 성공"));
    }

    @DeleteMapping("/permanent")
    public ResponseEntity<MessageResponse> deletePermanentlyBulkAccount(@RequestBody AdminBulkAccountRequest request){
        accountService.deleteBulkAccount(request.getAccountIds(), true);
        return ResponseEntity.ok(MessageResponse.of("계정 영구 삭제 성공"));
    }

    @PostMapping("/restore")
    public ResponseEntity<MessageResponse> restoreBulkAccount(@RequestBody AdminBulkAccountRequest request){
        accountService.restoreBulkAccount(request.getAccountIds());
        return ResponseEntity.ok(MessageResponse.of("계정 복구 성공"));
    }
}
