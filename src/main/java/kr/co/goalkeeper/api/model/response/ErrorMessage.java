package kr.co.goalkeeper.api.model.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    @NonNull
    private int code;
    @NonNull
    private String message;
}
