package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, CategoryType> {
}
