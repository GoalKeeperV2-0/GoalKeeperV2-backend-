package kr.co.goalkeeper.api.exception;

import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GoalKeeperExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handle(RuntimeException e, HttpServletRequest request){
        if(e instanceof GoalkeeperException){
            GoalkeeperException exception = (GoalkeeperException) e;
            ErrorMessage errorMessage = exception.getErrorMessage();
            errorMessage.setRequestId((String)request.getAttribute("uuid"));
            return ResponseEntity.status(errorMessage.getCode()).body(errorMessage);
        }else {
            log.error(e.toString());
            ErrorMessage errorMessage = new ErrorMessage(500,"알수 없는 이유로 요청이 정상처리 되지 않았습니다.");
            errorMessage.setRequestId((String)request.getAttribute("uuid"));
            return ResponseEntity.status(errorMessage.getCode()).body(errorMessage);
        }
    }
}
