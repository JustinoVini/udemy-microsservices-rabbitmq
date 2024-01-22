package br.com.udemy.productapi.modules.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.udemy.productapi.modules.supplier.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
