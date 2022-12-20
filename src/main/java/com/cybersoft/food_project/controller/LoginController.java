package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.jwt.JwtTokenHelper;
import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.cybersoft.food_project.payload.response.DataTokenResponse;
import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/signin")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    private long expiredDate = 8 * 60 * 60 * 1000; // đổi 8 tiếng ra mili giây
    private long refreshExpiredDate = 80 * 60 * 60 * 1000; // đổi 8 tiếng ra mili giây

    @GetMapping("/test")
    public String test(){
        return "Hello ABC";
    }

    @PostMapping("")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authRequest); // nếu dược xác thực thành công bên CustomAuthenProvider thì sẽ có giá trị
                                                                               //  auth --> nếu ko thì code sẽ dừng và quăng ra 403
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        String token = jwtTokenHelper.generateToken(signInRequest.getUsername(),"authen", expiredDate); // tạo token lưu username
        String refreshToken = jwtTokenHelper.generateToken(signInRequest.getUsername(),"refresh", refreshExpiredDate);

//        String decodeToken = jwtTokenHelper.decodeToken(token);

        DataTokenResponse dataTokenResponse = new DataTokenResponse();
        dataTokenResponse.setToken(token);
        dataTokenResponse.setRefreshToken(refreshToken);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDescription("");
        dataResponse.setData(dataTokenResponse);
        dataResponse.setSuccess(loginService.checkLogin(signInRequest.getUsername(), signInRequest.getPassword()));

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
