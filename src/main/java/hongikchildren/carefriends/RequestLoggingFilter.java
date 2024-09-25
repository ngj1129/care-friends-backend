package hongikchildren.carefriends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 정보 출력
        logger.info("---- HTTP Request ----");
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("HTTP Method: {}", request.getMethod());
        logger.info("Query String: {}", request.getQueryString());

        // 헤더 출력
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("Header: {} = {}", headerName, request.getHeader(headerName));
        }

        // 파라미터 출력
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            logger.info("Param: {} = {}", paramName, request.getParameter(paramName));
        }

        // 필터 체인 계속 실행
        filterChain.doFilter(request, response);

        // 응답 정보 출력
        logger.info("---- HTTP Response ----");
        logger.info("Response Status: {}", response.getStatus());
    }

    @Override
    public void destroy() {
        // 필터 종료 시 처리할 작업
    }

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // 필터 초기화 시 처리할 작업
//    }
}

