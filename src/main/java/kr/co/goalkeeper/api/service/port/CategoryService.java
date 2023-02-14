package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.CategoryType;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();
}
