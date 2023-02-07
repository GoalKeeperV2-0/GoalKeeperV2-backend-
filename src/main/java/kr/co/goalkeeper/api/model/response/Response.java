package kr.co.goalkeeper.api.model.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Response<T> {
    @NonNull
    private String message;
    @NonNull
    private T data;

}
