package kr.co.goalkeeper.api.model.entity;

import javax.persistence.*;

@Entity
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
