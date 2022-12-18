package com.cybersoft.food_project.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenHelper {

    private long expiredDate = 8 * 60 * 60 * 1000; // đổi 8 tiếng ra mili giây
    private final String strKey = "JUMqRi1KYU5kUmdValhuMnI1dTh4L0E/RChHK0tiUGU="; // chuỗi base 64 (lưu ý phải lớn hơn 256 bits nếu ko sẽ lỗi)
    private SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));
    public String generateToken(String data){
        Date now = new Date();
        Date dateExpired = new Date(now.getTime() + expiredDate);


        return Jwts.builder()
                .setSubject(data) // lưu trữ dữ liệu vào trong token kiểu String
                .setIssuedAt(now) // thời gian tạo ra token
                .setExpiration(dateExpired) // thời gian hết hạn của token
                .signWith(secretKey, SignatureAlgorithm.HS256) // thuật toán mã hoá và secretkey
                .compact(); // trả ra token đã được mã hoá kiểu String nên return về token luôn
    }

    public String decodeToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject(); // lấy data đã chuyển thành token ở trên ngoài ra còn .getIssuedAt, .getExpiration

    }
}
