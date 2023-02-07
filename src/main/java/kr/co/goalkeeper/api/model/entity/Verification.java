package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cert_id")
    private Certification certification;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private boolean state;

    public Verification(Certification certification, User user, boolean state) {
        this.certification = certification;
        this.user = user;
        this.state = state;
        if(state){
            certification.verificationSuccess();
        }else {
            certification.verificationFail();
        }
    }
}
