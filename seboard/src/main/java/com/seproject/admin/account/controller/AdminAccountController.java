package com.seproject.admin.account.controller;

import com.seproject.admin.account.application.AdminAccountAppService;
import com.seproject.admin.account.controller.condition.AccountCondition;
import com.seproject.board.common.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.seproject.admin.account.controller.dto.AdminAccountDto.*;

@Tag(name = "계정 관리 API", description = "관리자 시스템이 갖는 계정 관리 API")
@RequiredArgsConstructor
@RequestMapping(value = "/admin/accounts")
@RestController
public class AdminAccountController {

    private final AdminAccountAppService accountAppService;
    @Operation(summary = "모든 계정 목록 조회", description = "계정 관리를 위하여 등록된 계정 목록을 확인한다.")
    @GetMapping
    public ResponseEntity<Page<AccountResponse>> retrieveAllAccount(@ModelAttribute AccountCondition condition,
                                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "perPage", defaultValue = "25") int perPage) {
        Page<AccountResponse> response = accountAppService.findAllAccount(condition, page, perPage);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "계정 상세 조회", description = "계정의 상세 정보를 조회한다.")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> retrieveAccount(@PathVariable Long accountId) {
        AccountResponse response = accountAppService.findAccount(accountId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @Operation(summary = "계정 생성", description = "관리자는 계정을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
           accountAppService.createAccount(createAccountRequest);
           return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "계정 수정", description = "관리자는 계정을 수정할 수 있다.")
    @PutMapping("/{accountId}")
    public ResponseEntity<Void> updateAccount(@RequestBody UpdateAccountRequest request, @PathVariable Long accountId) {
        accountAppService.updateAccount(accountId,request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "계정 삭제", description = "관리자는 계정을 삭제할 수 있다.")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountAppService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "삭제된 계정 조회", description = "삭제된 계정 목록을 조회한다.")
    @GetMapping("/deleted")
    public ResponseEntity<Page<AccountResponse>> retrieveAccount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int perPage)  {

        AccountCondition condition = new AccountCondition();
        condition.setStatus(Status.TEMP_DELETED);
        Page<AccountResponse> response = accountAppService.findAllAccount(condition, page, perPage);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    @DeleteMapping
//    public ResponseEntity<MessageResponse> deleteBulkAccount(@RequestBody AdminBulkAccountRequest request){
//        accountService.deleteBulkAccount(request.getAccountIds(), false);
//        return ResponseEntity.ok(MessageResponse.of("계정 삭제 성공"));
//    }
//
//    @DeleteMapping("/permanent")
//    public ResponseEntity<MessageResponse> deletePermanentlyBulkAccount(@RequestBody AdminBulkAccountRequest request){
//        accountService.deleteBulkAccount(request.getAccountIds(), true);
//        return ResponseEntity.ok(MessageResponse.of("계정 영구 삭제 성공"));
//    }
//
//    @PostMapping("/restore")
//    public ResponseEntity<MessageResponse> restoreBulkAccount(@RequestBody AdminBulkAccountRequest request){
//        accountService.restoreBulkAccount(request.getAccountIds());
//        return ResponseEntity.ok(MessageResponse.of("계정 복구 성공"));
//    }
}
