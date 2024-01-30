import amqp from "amqplib/callback_api.js";

import * as secrets from "../../../config/secrets/secrets.js";
import * as queue from "../../../config/rabbitmq/queue.js";

export function sendProductStockUpdateQueue(message) {
    amqp.connect(secrets.RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }
            let jsonStringMessage = JSON.stringify(message);
            console.log(`Sending message to product update stock: ${jsonStringMessage}`);
            channel.publish(
                queue.PRODUCT_TOPIC,
                queue.PRODUCT_STOCK_UPDATE_ROUTING_KEY,
                Buffer.from(jsonStringMessage));
        });
        console.log("Message was sent successfully.");
    });
}