package kr.co.goalkeeper.api.model.domain;

import javax.persistence.*;

@Entity
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
