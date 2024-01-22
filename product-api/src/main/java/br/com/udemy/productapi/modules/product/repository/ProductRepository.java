package br.com.udemy.productapi.modules.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.udemy.productapi.modules.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
