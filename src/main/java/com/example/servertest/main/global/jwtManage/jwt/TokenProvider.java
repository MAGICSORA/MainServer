package com.example.servertest.main.global.jwtManage.jwt;

import com.example.servertest.main.global.jwtManage.entity.RefreshToken;
import com.example.servertest.main.global.jwtManage.model.Token;
import com.example.servertest.main.member.service.AuthorityService;
import com.example.servertest.main.member.type.MemberType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final AuthorityService authorityService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    //토큰 유효시간 설정
    private Long tokenValidTime = 240 * 60 * 1000L;
//    private Long accessTokenValidTime = Duration.ofMinutes(30).toMillis(); //30분
    private Long accessTokenValidTime = Duration.ofMinutes(2).toMillis(); //2분
    private Long refreshTokenValidTime = Duration.ofDays(14).toMillis(); //2주

    //secretkey를 미리 인코딩 해준다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generatedToken(String email, MemberType role) {
        // payload 설정
        // registerd clims
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject("access_token") // 토큰 제목
                .setIssuedAt(now) // 발행시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)); // 토큰 만료 기한

        // private claims
        claims.put("email", email); // 정보는 key - value 쌍으로 저장
        claims.put("role", role);

        return Jwts.builder().setHeaderParam("typ", "JWT") // 헤어
                .setClaims(claims) // 페이로드
                .signWith(SignatureAlgorithm.HS256,
                        secretKey) // 서명, 사용할 암호화 알고리즘과 signature 에 들어갈 secretKey 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authorityService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .get("email");
    }

    // Request의 Header에서 token 값을 가져 옴
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("JWT");
    }

    // 토큰의 유효성 + 만료일자 확인  // -> 토큰이 expire되지 않았는지 True/False로 반환해줌.
    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Token createAccessToken(String email, String role) {

        Date now = new Date();
        Claims claims1 = Jwts.claims().setSubject("access_token") // 토큰 제목
                .setIssuedAt(now) // 발행시간
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)); // 토큰 만료 기한

        claims1.put("email", email); // 정보는 key - value 쌍으로 저장
        claims1.put("role", role);

        Claims claims2 = Jwts.claims().setSubject("refresh_token") // 토큰 제목
                .setIssuedAt(now) // 발행시간
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)); // 토큰 만료 기한

        claims2.put("email", email); // 정보는 key - value 쌍으로 저장
        claims2.put("role", role);

        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims1) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        //Refresh Token
        String refreshToken = Jwts.builder()
                .setClaims(claims2) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

//        refreshTokenRepository.save(refreshToken);

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(email).build();
    }

    public String validateRefreshToken(RefreshToken refreshTokenObj) {

        // refresh 객체에서 refreshToken 추출
        String refreshToken = refreshTokenObj.getRefreshToken();

        try {
            // 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            System.out.println("test3");
//            System.out.println(claims.getBody().get("role"));

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
            if (!claims.getBody().getExpiration().before(new Date())) {
                System.out.println("test4");
                return recreationAccessToken(claims.getBody().get("email").toString(), claims.getBody().get("role").toString());
            }
        } catch (Exception e) {
            System.out.println("test1");
            e.printStackTrace();
            //refresh 토큰이 만료되었을 경우, 로그인이 필요합니다.


            return null;
        }
        System.out.println("test2");
        return null;
    }

    public String recreationAccessToken(String email, String role) {

        Claims claims = Jwts.claims().setSubject("access_token"); // JWT payload 에 저장되는 정보단위
        claims.put("email", email);
        claims.put("role", role); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return accessToken;
    }
}
