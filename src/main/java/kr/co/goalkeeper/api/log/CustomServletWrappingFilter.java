package kr.co.goalkeeper.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
@Slf4j
public class CustomServletWrappingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappingRequest, wrappingResponse);
        String uuid = (String)wrappingRequest.getAttribute("uuid");
        wrappingResponse.addHeader("requestId",uuid);
        String type="Response ===> uuid = ";
        log.info("{}{} {}",type,uuid,"headers start");
        Collection<String> headerNames = wrappingResponse.getHeaderNames();
        for (String headerName : headerNames) {
            String header = wrappingResponse.getHeader(headerName);
            log.info("{}{} {}: {}",type,uuid,headerName,header);
        }
        log.info("{}{} {}",type,uuid,"headers end");
        wrappingResponse.copyBodyToResponse();
    }
}
