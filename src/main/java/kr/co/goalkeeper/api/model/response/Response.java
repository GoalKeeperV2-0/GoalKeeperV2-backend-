package kr.co.goalkeeper.api.model.response;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class Response<T> {
    @NonNull
    private String message;
    @NonNull
    private T data;

}
