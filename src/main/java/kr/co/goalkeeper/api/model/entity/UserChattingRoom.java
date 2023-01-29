package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserChattingRoom {
    @EmbeddedId
    private UserChattingRoomId id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "chatting_room_id")
    @MapsId("chattingRoomId")
    private ChattingRoom chattingRoom;
}
