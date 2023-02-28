package kr.co.goalkeeper.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Aspect
@Component
public class BusinessLogicLogger {

    @Around("execution(* kr.co.goalkeeper.api..*Controller.*(..)) " +
            "|| execution(* kr.co.goalkeeper.api..*Service.*(..)) || execution(* kr.co.goalkeeper.api..*Repository.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String type;
        if (className.contains("Controller")) {
            printRequest();
            type = "Controller ";
        } else if (className.contains("Service")) {
            type = "Service";
        } else if (className.contains("Repository")) {
            type = "Repository";
        } else {
            type = "";
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uuid = (String) request.getAttribute("uuid");
            log.info("{} {} {} ===> {}.{}()", "Request ===> uuid = ", uuid, type, className, methodName);
        }catch (IllegalStateException e){
            log.info("{} {} {} ===> {}.{}()", "Scheduler ===> className = ",className,type,className,methodName );
        }
        return joinPoint.proceed();
    }
    private void printRequest() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uuid = (String)request.getAttribute("uuid");
        log.info("Request ===> uuid = " + uuid + " uri = "+request.getRequestURI());
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        log.info("Request ===> uuid = " + uuid + " headers");
        ObjectMapper objectMapper = new ObjectMapper();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            log.info("Request ===> uuid = " + uuid +" "+ headerName+": "+header);
        }
        log.info("Request ===> uuid = " + uuid + " body = " + objectMapper.readTree(cachingRequest.getContentAsByteArray()));
    }

}
