package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.repository.CategoryRepository;
import kr.co.goalkeeper.api.service.port.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;

    public SimpleCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }
}
