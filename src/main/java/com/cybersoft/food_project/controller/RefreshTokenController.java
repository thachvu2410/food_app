package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.jwt.JwtTokenFilter;
import com.cybersoft.food_project.jwt.JwtTokenHelper;
import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.cybersoft.food_project.payload.response.DataTokenResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/refresh-token")
public class RefreshTokenController {
    private final Gson gson = new Gson();
    private long expiredDate = 8 * 60 * 60 * 1000; // đổi 8 tiếng ra mili giây
    private long refreshExpiredDate = 80 * 60 * 60 * 1000; // đổi 8 tiếng ra mili giây
    @Autowired
    JwtTokenHelper jwtTokenHelper;


    @PostMapping("")
    public ResponseEntity<?> index(@RequestParam("token") String token){
        DataResponse dataResponse = new DataResponse();

        if (jwtTokenHelper.validateToken(token)){
            String json = jwtTokenHelper.decodeToken(token);
            Map<String, Object> map = gson.fromJson(json, Map.class);

            if (StringUtils.hasText(map.get("type").toString())
                    && map.get("type").toString().equals("refresh")){
                System.out.println("kiem tra " + json + " - " + map.get("type").toString());
                String tokenAuthen = jwtTokenHelper.generateToken(map.get("username").toString(),"authen", expiredDate);
                String refreshToken = jwtTokenHelper.generateToken(map.get("username").toString(),"refresh", refreshExpiredDate);

                DataTokenResponse dataTokenResponse = new DataTokenResponse();
                dataTokenResponse.setToken(tokenAuthen);
                dataTokenResponse.setRefreshToken(refreshToken);

                dataResponse.setStatus(HttpStatus.OK.value());
                dataResponse.setDescription("");
                dataResponse.setData(dataTokenResponse);
                dataResponse.setSuccess(true);
            }
        }else {
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setDescription("");
            dataResponse.setData("");
            dataResponse.setSuccess(true);
        }

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
