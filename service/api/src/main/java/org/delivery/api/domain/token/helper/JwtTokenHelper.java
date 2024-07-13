package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper implements TokenHelperIfs { // Jwt 토큰을 검증하고 생성하는

    @Value("${token.secret.key}") //yaml 정의된 값 넣기 //스프링 부트가 실행되고 빈으로 만들어질 때 채워짐
    private String secretKey;

    @Value("${token.access-token.plus-hour}") // 접근 토큰 유효기간
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}") // 리프레시 토큰 유효기간
    private Long refreshTokenPlusHour;

    @Override
    public TokenDto issueAcessToken(Map<String, Object> data) { // 접근 토큰 생성
        var expiredLocalDateTime= LocalDateTime.now().plusHours(accessTokenPlusHour); // 토큰의 만료 시간

        var expiredAt= Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()); //LocalDateTime->Date

        var key= Keys.hmacShaKeyFor(secretKey.getBytes()); // 비밀 키를 바탕으로 HMAC SHA 키 생성 // 바이트 배열로 변환
        // 보안성 높은 암호화 방법


        // JWT는 헤더, 페이로드, 서명(Signature) 로 구성된다.

        var jwtToken= Jwts.builder() // 토큰 생성
                .signWith(key)
                .signWith(key, SignatureAlgorithm.HS256) // 토큰의 서명 설정
                .setClaims(data) // 데이터를 클레임으로 설정, 클레임은 JWT 페이로드에 포함되는 정보, 사용자 식별 정보, 권한, 기타 메타데이터 포함
                .setExpiration(expiredAt) // JWT의 만료시간을 설정
                .compact();

        return TokenDto.builder() // token -> 데이터 전송 객체
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        var expiredLocalDateTime= LocalDateTime.now().plusHours(refreshTokenPlusHour); // 토큰의 만료 시간

        var expiredAt= Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()); // Date

        var key= Keys.hmacShaKeyFor(secretKey.getBytes()); // 키 만들기

        var jwtToken= Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) { // JWT 토큰 검증 유효성 확인
        var key= Keys.hmacShaKeyFor(secretKey.getBytes()); // 비밀 키 바탕으로 hmac sha 키 생성

        var parser= Jwts.parser() // jwt 토큰의 유효성 검사를 위한 파서 생성 //
                //파서의 역할: 1. JWT 문자열 파싱 2. 서명 검증, 3. 페이로드 클레임 추출
                .setSigningKey(key) // 서명 검증을 위한 키
                .build();

        try{
            var result = parser.parseClaimsJws(token); // 토큰 문자열 파싱, 서명 검증, 클레임 추출 result는 Jws<Claims> 형식

            return new HashMap<String, Object>(result.getBody());

        }catch (Exception e){

            if(e instanceof SignatureException){
                // 토큰이 유효하지 않을때
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
            }
            else if(e instanceof ExpiredJwtException){
                //  만료된 토큰
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);
            }
            else{
                // 그외 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION, e);
            }
        }
    }
}
