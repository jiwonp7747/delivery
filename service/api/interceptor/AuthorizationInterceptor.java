package org.delivery.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
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

        // TODO header 검증
        return true;
    }
}
