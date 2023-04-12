package com.seproject.oauth2.controller;

import com.seproject.oauth2.utils.JwtDecoder;
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
        return "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"/\" method = \"POST\">\n" +
                "<input type=\"text\" name=\"jwt\" id=\"jwt\">\n" +
                "\n" +
                "<button>send</button>\n" +
                "</form>\n" +
                "</body>\n" +
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
