package kr.co.goalkeeper.api.model.entity;


import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class UserChattingRoomId implements Serializable {
    private long userId;
    private long chattingRoomId;
}
