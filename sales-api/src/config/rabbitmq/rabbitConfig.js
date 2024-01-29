import amqp from "amqplib/callback_api.js";

import * as secrets from "../secrets/secrets.js";
import * as queue from "./queue.js";

const HALD_SECOND = 500;

export async function connectRabbitMq() {
    amqp.connect(secrets.RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        createQueue(connection, queue.SALES_CONFIRMATION_QUEUE, queue.SALES_CONFIRMATION_ROUTING_KEY, queue.PRODUCT_TOPIC);
        createQueue(connection, queue.PRODUCT_STOCK_UPDATE_QUEUE, queue.PRODUCT_STOCK_UPDATE_ROUTING_KEY, queue.PRODUCT_TOPIC);
        setTimeout(function () {
            connection.close();
        }, HALD_SECOND);
    });

}

function createQueue(connection, queue, routingKey, topic) {
    connection.createChannel((error, channel) => {
        if (error) {
            throw error;
        }
        channel.assertExchange(topic, "topic", { durable: true });
        channel.assertQueue(queue, { durable: true });
        channel.bindQueue(queue, topic, routingKey);
    });
}