package kr.co.goalkeeper.api.model.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    @Setter
    private String requestId;
    @NonNull
    private int code;
    @NonNull
    private String message;
}
