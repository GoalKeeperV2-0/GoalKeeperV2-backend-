package kr.co.goalkeeper.api.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Response<T> {
    @Setter
    private String requestId;
    @NonNull
    private String message;
    @NonNull
    private T data;

}
