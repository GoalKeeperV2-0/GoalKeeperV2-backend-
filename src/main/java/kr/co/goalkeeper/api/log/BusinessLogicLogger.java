package kr.co.goalkeeper.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Aspect
@Component
@Order(1)
public class BusinessLogicLogger {

    @Around("execution(* kr.co.goalkeeper.api..*Controller.*(..)) " +
            "|| execution(* kr.co.goalkeeper.api..*Service.*(..)) " +
            "|| execution(* kr.co.goalkeeper.api..*Repository.*(..)) " +
            "|| execution(* kr.co.goalkeeper.api.util..*(..)) " +
            "|| execution(* kr.co.goalkeeper.api.aop..*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String type = getBusinessType(className);
        if (className.contains("Controller")) {
            printRequestBody();
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String requestId = (String) request.getAttribute("requestId");
            log.info("{} = {} {} {}.{}()","RequestId",requestId,type,className,methodName);
        }catch (IllegalStateException e){
            log.info("{} = {} {} {}.{}()","Scheduler",className,type,className,methodName);
        }
        return joinPoint.proceed();
    }
    private String getBusinessType(String className){
        if(className.contains("Controller")){
            return "<<Controller>>";
        }
        if(className.contains("Service")){
            return "<<Service>>";
        }
        if(className.contains("Repository")){
            return "<<Repository>>";
        }
        if(className.contains("util")){
            return "<<Util>>";
        }
        if(className.contains("aop")){
            return "<<AOP>>";
        }
        return "";
    }
    private void printRequestBody() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        String requestId = (String) cachingRequest.getAttribute("requestId");
        String logType = "<<Request>>";
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("{} = {} {} {} = \n{}","RequestId",requestId,logType,"Body",objectMapper.readTree(cachingRequest.getContentAsByteArray()).toPrettyString());
    }
}
