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
import java.util.UUID;

@Component
@Slf4j
public class ResponseLoggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uuid = UUID.randomUUID().toString();
        request.setAttribute("uuid",uuid);
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappingRequest, wrappingResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String type="Response ===> uuid = ";
        log.info("{}{} {}",type,uuid,"headers start");
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            String header = response.getHeader(headerName);
            log.info("{}{} {}: {}",type,uuid,headerName,header);
        }
        log.info("{}{} {}",type,uuid,"headers end");
        log.info("{}{}, Response Body : {}",type, uuid, objectMapper.readTree(wrappingResponse.getContentAsByteArray()));
        wrappingResponse.copyBodyToResponse();
    }
}
