package br.com.udemy.productapi.modules.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.udemy.productapi.config.exception.ValidationException;
import br.com.udemy.productapi.modules.category.service.CategoryService;
import br.com.udemy.productapi.modules.product.dto.ProductRequest;
import br.com.udemy.productapi.modules.product.dto.ProductResponse;
import br.com.udemy.productapi.modules.product.model.Product;
import br.com.udemy.productapi.modules.product.repository.ProductRepository;
import br.com.udemy.productapi.modules.supplier.service.SupplierService;

@Service
public class ProductService {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    public ProductResponse save(ProductRequest request) {
        validateProductData(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    private void validateProductData(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The product's name was not found");
        }
        if (isEmpty(request.getQuantityAvailable())) {
            throw new ValidationException("The product's quantity was not informed");
        }
        if (isEmpty(request.getQuantityAvailable() <= ZERO)) {
            throw new ValidationException("The quantity should not be less or equal to zero");
        }
    }

    private void validateCategoryAndSupplierInformed(ProductRequest request) {
        if (isEmpty(request.getCategoryId())) {
            throw new ValidationException("The category id was not informed");
        }
        if (isEmpty(request.getSupplierId())) {
            throw new ValidationException("The supplier id was not informed");
        }
    }

}
