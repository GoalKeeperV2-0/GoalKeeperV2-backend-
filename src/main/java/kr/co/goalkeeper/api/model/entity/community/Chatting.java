package kr.co.goalkeeper.api.model.entity.community;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull
    private String data;
    @Column
    @Enumerated(EnumType.STRING)
    private ChattingType type;
    private long good;
    private long see;
    @JoinColumn(name = "chatting_room_id")
    @ManyToOne
    private ChattingRoom chattingRoom;
}
