package kr.co.goalkeeper.api.model.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class UserCategoryPointId implements Serializable {
    private long userId;
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;
}
