package kr.co.goalkeeper.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
@AllArgsConstructor
@Getter
public class ErrorMessage {
    private int code;
    @NonNull
    private String message;
}
