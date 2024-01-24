package br.com.udemy.productapi.modules.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.udemy.productapi.config.exception.SuccessResponse;
import br.com.udemy.productapi.config.exception.ValidationException;
import br.com.udemy.productapi.modules.category.service.CategoryService;
import br.com.udemy.productapi.modules.product.dto.ProductQuantityDTO;
import br.com.udemy.productapi.modules.product.dto.ProductRequest;
import br.com.udemy.productapi.modules.product.dto.ProductResponse;
import br.com.udemy.productapi.modules.product.dto.ProductStockDTO;
import br.com.udemy.productapi.modules.product.model.Product;
import br.com.udemy.productapi.modules.product.repository.ProductRepository;
import br.com.udemy.productapi.modules.sales.dto.SalesConfirmationDTO;
import br.com.udemy.productapi.modules.sales.enums.SalesStatus;
import br.com.udemy.productapi.modules.sales.rabbitmq.SalesConfirmationSender;
import br.com.udemy.productapi.modules.supplier.service.SupplierService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private SupplierService supplierService;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    public List<ProductResponse> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw new ValidationException("The supplier name must be informed");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isEmpty(supplierId)) {
            throw new ValidationException("The product's supplier Id must be informed");
        }
        return productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isEmpty(categoryId)) {
            throw new ValidationException("The product's category Id must be informed");
        }
        return productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(findById(id));
    }

    public Product findById(Integer id) {
        validateInformedId(id);
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given ID."));
    }

    public ProductResponse save(ProductRequest request) {
        validateProductData(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request, Integer id) {
        validateProductData(request);
        validateInformedId(id);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(product);
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

    public Boolean existsByCategoryId(Integer categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId) {
        return productRepository.existsBySupplierId(supplierId);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted");
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The product ID must be informed");
        }
    }

    public void updateProductStock(ProductStockDTO product) {
        try {
            validateStockUpdateData(product);
            updateStock(product);
        } catch (Exception e) {
            log.error("Error while trying to update stock for message with error: {}", e.getMessage(), e);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED);
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    private void validateStockUpdateData(ProductStockDTO product) {
        if (isEmpty(product) || isEmpty(product.getSalesId())) {
            throw new ValidationException("The product data or sales ID must be informed");
        }

        if (isEmpty(product.getProducts())) {
            throw new ValidationException("The sales products must be informed");
        }

        product.getProducts().forEach(salesProduct -> {
            if (isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())) {
                throw new ValidationException("The productID and the quantity must be informed");
            }
        });
    }

    private void updateStock(ProductStockDTO product) {
        product.getProducts().forEach(salesProduct -> {
            var existingProduct = findById(salesProduct.getProductId());
            validateQuantityInStock(salesProduct, existingProduct);
            existingProduct.updateStock(salesProduct.getQuantity());
            productRepository.save(existingProduct);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED);
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        });
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
        if (salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
            throw new ValidationException(
                    String.format("The product %s is out of stock.", existingProduct.getId()));
        }
    }

}
