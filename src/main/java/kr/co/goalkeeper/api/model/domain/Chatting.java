package kr.co.goalkeeper.api.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
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
