package com.cybersoft.food_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig {

    @Autowired
    CustomAuthenProvider customAuthenProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(customAuthenProvider);

        return authenticationManagerBuilder.build();
    }



    // Dùng để khời tạo danh sách user cứng và lưu trên RAM
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails user1 = User.withUsername("cybersoft")
//                .password(passwordEncoder().encode("123"))
//                .roles("USERS")
//                .build();
//        UserDetails user2 = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    // hàm mã hoá dữ liệu
    // đối tượng BCryptPasswordEncoder là con của PasswordEncoder

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Quy định các rule liên quan tới bảo mật và quyền truy cập
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        /*
        authorizeRequests: luôn luôn phải thông qua trường chứng thực này
        antMatchers: khai báo các link yêu cần xác thực mới được truy cập vào
        anyRequest: khai báo tất cả các link mà ko cần chỉ định cụ thể trừ link antMatchers đã chỉ định
        permitAll: cho phép truy cập mà ko cần chứng thực (full quyền vào link chỉ định)
        authenticated: bắt buộc phải chứng thực
         */
        http.csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/signin/test").authenticated()
                //.permitAll() // vì link signin chưa đăng nhập lấy gì mà chứng
                .anyRequest().authenticated();

        return http.build();
    }

}
