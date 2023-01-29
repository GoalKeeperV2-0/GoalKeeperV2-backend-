package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "cert_id")
    private Certification certification;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean success;
}
