package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
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

    @GetMapping("/test")
    public String test(){
        return "Hello ABC";
    }


    @PostMapping("")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){


        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDescription("");
        dataResponse.setData("");
        dataResponse.setSuccess(loginService.checkLogin(signInRequest.getUsername(), signInRequest.getPassword()));

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
