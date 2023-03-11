package kr.co.goalkeeper.api.exception;

import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class GoalKeeperExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handle(RuntimeException e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log.error("\n"+sw);
        if(e instanceof GoalkeeperException){
            GoalkeeperException exception = (GoalkeeperException) e;
            ErrorMessage errorMessage = exception.getErrorMessage();
            return ResponseEntity.status(errorMessage.getCode()).body(errorMessage);
        }else {
            ErrorMessage errorMessage = new ErrorMessage(500,"알수 없는 이유로 요청이 정상처리 되지 않았습니다.");
            return ResponseEntity.status(errorMessage.getCode()).body(errorMessage);
        }
    }
}
