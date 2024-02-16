package br.com.udemy.productapi.modules.product.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.udemy.productapi.modules.product.dto.ProductStockDTO;
import br.com.udemy.productapi.modules.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockListener {

    private final ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
        log.info("Recieving message with data: {} and TransactionID: {}",
                new ObjectMapper().writeValueAsString(product),
                product.getTransactionid());
        productService.updateProductStock(product);
    }

}
