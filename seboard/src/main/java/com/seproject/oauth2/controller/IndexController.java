package com.seproject.oauth2.controller;

import com.seproject.oauth2.utils.jwt.JwtDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@AllArgsConstructor
@RestController
public class IndexController {

    private JwtDecoder jwtDecoder;

    @GetMapping("/index")
    public String index() {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "    <body>\n" +
                "        <h1>jwt decode</h1>\n" +
                "            <form action=\"/\" method = \"POST\">\n" +
                "            <input type=\"text\" name=\"jwt\" id=\"jwt\">\n" +
                "\n" +
                "            <button>send</button>\n" +
                "            </form>\n" +
                "        <hr/>\n" +
                "\n" +
                "        <h1>로그인</h1>\n" +
                "        <!-- 시큐리티는 x-www-form-url-encoded 타입만 인식 -->\n" +
                "            <form action=\"/formLogin\" method=\"post\">\n" +
                "                id:  <input type=\"text\" name=\"username\" />\n" +
                "                </br>\n" +
                "                pwd: <input type=\"password\" name=\"password\" />\n" +
                "                </br>\n" +
                "                <button>로그인</button>\n" +
                "            </form>\n" +
                "        <a href=\"/oauth2/authorization/kakao\" >\n" +
                "            <img src=\"kakao_login_medium_narrow.png\"\n" +
                "                 alt=\"kakao\">\n" +
                "        </a>\n" +
                "\n" +
                "    </body>\n" +
                "</html>";
    }

    @PostMapping("/index")
    public ResponseEntity<?> jwtDecode(HttpServletRequest request){
        String jwt = request.getHeader("Authorization");

        Map<String,Object> result = new HashMap<>();
        String id = jwtDecoder.getLoginId(jwt);
        String provider = jwtDecoder.getProvider(jwt);
        String email = jwtDecoder.getEmail(jwt);
        String profile = jwtDecoder.getProfile(jwt);
        List<String> authorities = jwtDecoder.getAuthorities(jwt);
        result.put("id" ,id);
        result.put("provider" ,provider);
        result.put("email" ,email);
        result.put("profile" ,profile);
        result.put("authorities" ,authorities);
        System.out.println(jwt);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
