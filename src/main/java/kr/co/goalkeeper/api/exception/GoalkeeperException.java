package kr.co.goalkeeper.api.exception;

import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.Getter;

public class GoalkeeperException extends RuntimeException{
    @Getter
    private final ErrorMessage errorMessage;
    public GoalkeeperException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

}
