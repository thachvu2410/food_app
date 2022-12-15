package com.cybersoft.food_project.security;

import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Xử lí logic code đăng nhập thành công hay thất bại
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean isSucess = loginService.checkLogin(userName, password);

        if(isSucess){
            return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
