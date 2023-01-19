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
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LoggerAspect { //Todo 웹소켓 통신시 작동하는 핸들러에도 동작하도록 수정 필요.

    @Around("execution(* kr.co.goalkeeper.api..*Controller.*(..)) " +
            "|| execution(* kr.co.goalkeeper.api..*Service.*(..)) || execution(* kr.co.goalkeeper.api..*Repository.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String name = joinPoint.getSignature().getDeclaringTypeName();
        String type = "";
        if (name.contains("Controller")) {
            type = "Controller ===> ";
            String uuid = UUID.randomUUID().toString();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            request.setAttribute("uuid", uuid);
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
        } else if (name.contains("Service")) {
            type = "ServiceImpl ===> ";

        } else if (name.contains("Repository")) {
            type = "Repository ===> ";
        } else {
            type = "ChattingHandler ===> ";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("Request ===> uuid = " + request.getAttribute("uuid")+" "+type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }

}
