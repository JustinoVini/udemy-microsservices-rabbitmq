package br.com.udemy.productapi.modules.category.dto;

import org.springframework.beans.BeanUtils;

import br.com.udemy.productapi.modules.category.model.Category;
import lombok.Data;

@Data
public class CategoryResponse {

    private Integer id;
    private String description;

    public static CategoryResponse of(Category category) {
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }

}
