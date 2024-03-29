package br.com.udemy.productapi.modules.sales.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.udemy.productapi.modules.sales.dto.SalesConfirmationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesConfirmationSender {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDTO message) {
        try {
            log.info("Sending message: {}", objectMapper.writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
            log.info("Message was sent successfully");
        } catch (Exception e) {
            log.error("Error while trying to send sales confirmation message: ", e);
        }
    }

}
