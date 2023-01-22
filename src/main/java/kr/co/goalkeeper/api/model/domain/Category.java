package kr.co.goalkeeper.api.model.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Category {
    @Enumerated(EnumType.STRING)
    @Id
    private CategoryType categoryType;
}
