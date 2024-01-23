package br.com.udemy.productapi.modules.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.udemy.productapi.modules.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
}