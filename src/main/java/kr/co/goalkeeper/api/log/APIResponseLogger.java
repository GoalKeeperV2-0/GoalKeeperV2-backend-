package kr.co.goalkeeper.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
public class APIResponseLogger implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        requestURI = URLDecoder.decode(requestURI, StandardCharsets.UTF_8);
        if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) return;
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
        String uuid = (String) cachingRequest.getAttribute("uuid");
        String type="Response ===> uuid = ";
        Enumeration<String> headerNames = request.getHeaderNames();
        log.info("{}{} {}",type,uuid,"headers start");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            log.info("{}{} {}: {}",type,uuid,headerName,header);
        }
        log.info("{}{} {}",type,uuid,"headers end");
        if(ex==null) {
            log.info("{}{}, Response Body : {}",type, uuid, objectMapper.readTree(cachingResponse.getContentAsByteArray()));
        }else {
            log.error("{}{}, Error Body : {}",type, uuid, objectMapper.readTree(cachingResponse.getContentAsByteArray()));
        }
    }
}
