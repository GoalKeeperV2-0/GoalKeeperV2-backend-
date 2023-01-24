package kr.co.goalkeeper.api.model.domain;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserChattingRoomId implements Serializable {
    private long userId;
    private long chattingRoomId;
}
