package com.seproject.oauth2.controller;

import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.service.AccountService;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
import com.seproject.oauth2.utils.jwt.annotation.JWT;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

import static com.seproject.oauth2.controller.dto.AccountDTO.*;

@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminController {


    private final AccountService accountService;
    private final JwtDecoder jwtDecoder;
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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("페이지 번호가 잘못되었습니다.",HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 유저를 찾을수 없습니다.",HttpStatus.NOT_FOUND);
        }

    }

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
