package kr.co.goalkeeper.api.model.entity.community;

import kr.co.goalkeeper.api.model.entity.Category;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "category")
    @NotNull
    private Category category;
    @Column
    @Enumerated(EnumType.STRING)
    @NonNull
    private ChattingRoomLevel level;
}
