package kr.co.goalkeeper.api.model.entity.goal;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserCategoryPointId implements Serializable {
    @Column
    private long userId;
    @Column
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;
}
