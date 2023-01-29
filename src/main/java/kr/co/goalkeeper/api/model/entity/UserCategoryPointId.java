package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
@Getter
public class UserCategoryPointId implements Serializable {
    private long userId;
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;
}
