package com.cybersoft.food_project.security;

import com.cybersoft.food_project.entity.UserEntity;
import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {
    @Autowired
    LoginService loginService;
//    @Autowired
//    PasswordEncoder passwordEncoder; lỗi không thể lấy từ @Bean được

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Xử lí logic code đăng nhập thành công hay thất bại
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity user = loginService.checkLogin(userName);

        if(user != null){
            boolean isPasswordMatched = passwordEncoder.matches(password, user.getPassword());
            if (isPasswordMatched){
                return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
