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
import java.util.*;

@Component
@Slf4j
public class HttpLogger extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId",requestId);
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
        printRequestURIAndHeaders(wrappingRequest);
        filterChain.doFilter(wrappingRequest, wrappingResponse);
        wrappingResponse.addHeader("requestId",requestId);
        String responseBodyLog = makeResponseBodyLog(wrappingResponse);
        wrappingResponse.copyBodyToResponse();
        List<String> responseHeaderLogs = makeResponseHeaderLogs(requestId,response);
        printResponseLogs(requestId,responseHeaderLogs,responseBodyLog);
    }
    private void printRequestURIAndHeaders(ContentCachingRequestWrapper request){
        String logType = "<<Request>>";
        String requestId = (String)request.getAttribute("requestId");
        log.info("{} = {} {} {} = {}&{}","RequestId",requestId,logType,"URI",request.getRequestURI(),request.getQueryString());
        log.info("{} = {} {} {}","RequestId",requestId,logType,"Headers");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            log.info("{} = {} {} {} = {}","RequestId",requestId,logType,headerName,header);
        }
    }
    private String makeResponseBodyLog(ContentCachingResponseWrapper responseWrapper) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseWrapper.getContentAsByteArray()).toPrettyString();
    }
    private List<String> makeResponseHeaderLogs(String requestId, HttpServletResponse response){
        List<String> responseHeaderLogs = new ArrayList<>();
        responseHeaderLogs.add(String.format("%s = %s %s %s","RequestId",requestId,"<<Response>>","Headers"));
        for (String s : response.getHeaderNames()) {
            String headerLog = s + " = " + response.getHeader(s);
            responseHeaderLogs.add(headerLog);
        }
        return responseHeaderLogs;
    }
    private void printResponseLogs(String requestId,List<String> responseHeaderLogs, String responseBodyLog){
        for (String s : responseHeaderLogs) {
            log.info(s);
        }
        log.info("{} = {} {} {} = \n{}","RequestId",requestId,"<<Response>>","Body",responseBodyLog);
    }
}
