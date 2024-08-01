package org.delivery.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class LoggerFilter implements Filter { // Filter는 서비스 중 분석 도구로 사용

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //Request 내용을 읽으면 뒤에서 읽을 수 없기 때문에 래퍼 클래스 통해 캐싱
        var request=new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        var response=new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        log.info("INIT {}", request.getRequestURI());
        //뒤에 있는 컨트롤러, 인터셉터는 ContentCashingRequestWrapper로 랩핑된 객체가 넘어간다

        filterChain.doFilter(request, response); // 다음 필터나 서블릿(컨트롤러)로 전달 이 코드를 기준으로 요청 처리 전, 후 나뉨

        // request 정보
        // 헤더 정보
        var headerNames=request.getHeaderNames();
        var headerValues=new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey->{ // key-value 형식
            var headerValue=request.getHeader(headerKey);

            // authorization-token : ???, user-agent : ???
            headerValues.append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValue)
                    .append("] ");
        });
        var requestBody=new String(request.getContentAsByteArray());
        var uri=request.getRequestURI();
        var method=request.getMethod();
        log.info(">>> uri : {}, method: {}, header : {}, body : {}", uri, method, headerValues.toString(), requestBody);

        // response 정보
        // 헤더 정보
        var responseHeaderValues= new StringBuilder();

        response.getHeaderNames().forEach(headerKey->{
            var headerValue=response.getHeader(headerKey);
            responseHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValue)
                    .append("] ");
        });

        var responseBody= new String(response.getContentAsByteArray());
        log.info(">>> uri : {}, method: {}, header : {}, body : {}", uri, method, responseHeaderValues, responseBody);
        //responseBody 내용을 읽었기 때문에 초기화 과정
        response.copyBodyToResponse(); // 캐싱된 응답 데이터를 실제 http 응답으로 복사
    }
}
