package com.cybersoft.food_project.jwt;

import com.google.gson.Gson;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final Gson gson = new Gson();
    @Autowired
    JwtTokenHelper jwtTokenHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // cắt header và lấy token
        String token = getTokenFromHeader(request);
        if (token != null){
            // kiểm tra token có phải hệ thống sinh ra hay ko
            if (jwtTokenHelper.validateToken(token)){
                String json = jwtTokenHelper.decodeToken(token);
                Map<String, Object> map = gson.fromJson(json, Map.class);
                if (StringUtils.hasText(map.get("type").toString())
                        && !map.get("type").toString().equals("refresh")){
                    System.out.println("kiem tra " + json + " - " + map.get("type").toString());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", "", new ArrayList<>());
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authenticationToken);
                }
            }
        }else {

        }
//        System.out.println("Token " + token);
        filterChain.doFilter(request,response);
    }
    public String getTokenFromHeader(HttpServletRequest request){
        // lấy giá trị token ở header có key là Authorization
        String strToken = request.getHeader("Authorization");
        if (StringUtils.hasText(strToken) && strToken.startsWith("Bearer ")){
            // xử lí khi token hợp lệ
            // subString(): dùng để cắt chuỗi
            String finalToken = strToken.substring(7);
            return finalToken;
        }else {
            return null;
        }
    }
}
