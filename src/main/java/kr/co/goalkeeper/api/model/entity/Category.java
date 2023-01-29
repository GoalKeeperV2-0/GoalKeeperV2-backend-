package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
public class Category {
    @Enumerated(EnumType.STRING)
    @Id
    private CategoryType categoryType;
}
