package br.com.udemy.productapi.modules.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCheckStockRequest {

    List<ProductQuantityDTO> products;

}
