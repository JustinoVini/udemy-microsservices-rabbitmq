package br.com.udemy.productapi.modules.category.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.udemy.productapi.config.exception.ValidationException;
import br.com.udemy.productapi.modules.category.dto.CategoryRequest;
import br.com.udemy.productapi.modules.category.dto.CategoryResponse;
import br.com.udemy.productapi.modules.category.model.Category;
import br.com.udemy.productapi.modules.category.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw new ValidationException("The category description was not found");
        }
    }
    
}
