package com.seproject.oauth2.controller;

import com.seproject.oauth2.service.AccountService;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
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

import static com.seproject.oauth2.controller.dto.AccountDTO.*;

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
//    @JWT
    @GetMapping("/accounts")
    public ResponseEntity<?> retrieveAllAccount(HttpServletRequest request,@RequestBody RetrieveAllAccountRequest accountRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

        int page = accountRequest.getPage();
        int perPage = accountRequest.getPerPage();
        page = Math.max(page-1,0);
        perPage = Math.max(perPage,1);
        try{
            RetrieveAllAccountResponse allAccount = accountService.findAllAccount(page, perPage);
            return new ResponseEntity<>(allAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("페이지 번호가 잘못되었습니다.",HttpStatus.BAD_REQUEST);
        }

    }


    @Operation(summary = "등록된 계정 상세 조회", description = "계정의 상세 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAccountResponse.class)), responseCode = "200", description = "계정 상세 정보 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "존재하지 않는 계정")
    })
    //    @JWT
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> retrieveAllAccount(HttpServletRequest request,@PathVariable Long accountId) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

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
    //    @JWT
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccountByAdmin(HttpServletRequest request,@RequestBody CreateAccountRequest createAccountRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }
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
    //    @JWT
    @PutMapping("/accounts")
    public ResponseEntity<?> updateAccount(HttpServletRequest request,@RequestBody UpdateAccountRequest updateAccountRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

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
    //    @JWT
    @DeleteMapping("/accounts")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request,@RequestBody DeleteAccountRequest deleteAccountRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

        try{
            DeleteAccountResponse deleteAccount = accountService.deleteAccount(deleteAccountRequest.getAccountId());
            return new ResponseEntity<>(deleteAccount,HttpStatus.OK);
        }  catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 계정을 찾을수 없습니다.",HttpStatus.BAD_REQUEST);
        }

    }
}
