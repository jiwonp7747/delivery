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
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //Request 내용을 읽으면 뒤에서 읽을 수 없기 때문에 래퍼 클래스 통해 캐싱
        var request=new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        var response=new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        //뒤에 있는 컨트롤러, 인터셉터는 ContentCashingRequestWrapper로 랩핑된 객체가 넘어간다

        filterChain.doFilter(request, response); // 이 코드를 기준으로 위는 실행전, 아래는 실행 후

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
        response.copyBodyToResponse(); //쓰지 않을 경우 빈 response-body가 나간다.
    }
}
