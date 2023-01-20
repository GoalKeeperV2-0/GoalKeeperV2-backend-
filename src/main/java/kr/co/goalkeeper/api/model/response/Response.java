package kr.co.goalkeeper.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private String requestId;
    private T data;
}
