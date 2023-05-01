package com.seproject.admin.controller;

import com.seproject.account.service.AccountService;
import com.seproject.account.jwt.JwtDecoder;
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

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

import static com.seproject.admin.dto.AccountDTO.*;

@Tag(name = "계정 관리 API", description = "관리자 시스템이 갖는 계정 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminAccountController {

    private final AccountService accountService;
    private final JwtDecoder jwtDecoder;

    @Operation(summary = "등록된 계정 목록 조회", description = "계정 관리를 위하여 등록된 계정 목록을 확인한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllAccountResponse.class)), responseCode = "200", description = "계정 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 페이징 정보")
    })

    @GetMapping("/accounts")
    public ResponseEntity<?> retrieveAllAccount(HttpServletRequest request,@RequestBody RetrieveAllAccountRequest accountRequest) {
        int page = accountRequest.getPage();
        int perPage = accountRequest.getPerPage();
        page = Math.max(page-1,0);
        perPage = Math.max(perPage,1);
        RetrieveAllAccountResponse allAccount = accountService.findAllAccount(page, perPage);
        return new ResponseEntity<>(allAccount, HttpStatus.OK);
    }


    @Operation(summary = "등록된 계정 상세 조회", description = "계정의 상세 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAccountResponse.class)), responseCode = "200", description = "계정 상세 정보 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존재하지 않는 계정")
    })
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> retrieveAllAccount(HttpServletRequest request,@PathVariable Long accountId) {
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
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccountByAdmin(HttpServletRequest request,@RequestBody CreateAccountRequest createAccountRequest) {

        String email = createAccountRequest.getEmail();

        if(!email.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?")) {
            return new ResponseEntity<>("이메일 형식이 맞지 않습니다." , HttpStatus.BAD_REQUEST);
        }

       try{
           CreateAccountResponse account = accountService.createAccount(createAccountRequest);
           return new ResponseEntity<>(account,HttpStatus.OK);
       } catch (IllegalArgumentException e) {
           return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
       }

    }

    @Operation(summary = "계정 수정", description = "관리자는 계정을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateAccountResponse.class)), responseCode = "200", description = "계정 수정 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이메일 형식이 일치하지 않음"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "존재하지 않는 권한 요청"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존재하지 않는 계정")
    })
    @PutMapping("/accounts")
    public ResponseEntity<?> updateAccount(HttpServletRequest request,@RequestBody UpdateAccountRequest updateAccountRequest) {

        String email = updateAccountRequest.getEmail();
        if(!email.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?")) {
            return new ResponseEntity<>("이메일 형식이 맞지 않습니다." , HttpStatus.BAD_REQUEST);
        }

        try{
            UpdateAccountResponse updateAccount = accountService.updateAccount(updateAccountRequest);
            return new ResponseEntity<>(updateAccount,HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 계정을 찾을수 없습니다.",HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "계정 삭제", description = "관리자는 계정을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteAccountResponse.class)), responseCode = "200", description = "계정 삭제 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존자해지 않는 계정")
    })
    @DeleteMapping("/accounts")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request,@RequestBody DeleteAccountRequest deleteAccountRequest) {

        try{
            DeleteAccountResponse deleteAccount = accountService.deleteAccount(deleteAccountRequest.getAccountId());
            return new ResponseEntity<>(deleteAccount,HttpStatus.OK);
        }  catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 계정을 찾을수 없습니다.",HttpStatus.BAD_REQUEST);
        }

    }
}
