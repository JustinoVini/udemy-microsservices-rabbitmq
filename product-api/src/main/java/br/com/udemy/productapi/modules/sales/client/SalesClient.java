package br.com.udemy.productapi.modules.sales.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import br.com.udemy.productapi.modules.sales.dto.SalesProductResponse;

@FeignClient(name = "salesClient", contextId = "salesClient", url = "${app-config.services.sales}")
public interface SalesClient {

    @GetExchange("/product/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(@PathVariable Integer productId,
            @RequestHeader(name = "Authorization") String authorization,
            @RequestHeader(name = "transactionId") String transactionId);

}
