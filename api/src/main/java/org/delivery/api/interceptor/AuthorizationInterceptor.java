package org.delivery.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    @Override //사전 검증
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("Authorization Interceptor url: ", request.getRequestURI());

        // WEB, Chrom의 경우 get, post api를 요청하기 전에 options api를 요청하여 해당 메소드를 지원하는지 확인하는 메소드 존재
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        // API요청이 아니라 리소스(js, html, png) 을 요청하는 경우 pass
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }


        var accessToken=request.getHeader("authorization-token"); // 토큰 가져와서
        if(accessToken==null){ //없으면 예외처리
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        var userId=tokenBusiness.validationAccessToken(accessToken); // 있으면 id 가져오기

        if(userId!=null){ // id가 있으면
            // 한가지 요청에 대해 글로벌 하게 저장할 수 있는 스레드 로컬
            var requestContext= Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST); //request단위로 저장
            return true;
        }

        //없으면 예외
        throw new ApiException(ErrorCode.BAD_REQEUST, "인증 실패");
    }
}
