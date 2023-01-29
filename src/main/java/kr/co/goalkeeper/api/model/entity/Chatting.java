package kr.co.goalkeeper.api.model.entity;

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
    private long like;
    private long check;
    @JoinColumn(name = "chatting_room_id")
    @ManyToOne
    private ChattingRoom chattingRoom;
}
