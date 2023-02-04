package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.VerificationRequest;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cert_id")
    private Certification certification;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private boolean state;

    public Verification(long id, Certification certification, User user, boolean state) {
        this.id = id;
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
