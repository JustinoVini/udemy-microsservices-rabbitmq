import amqp from "amqplib/callback_api.js";
import { listenToConfirmationQueue } from "../../modules/sales/rabbitmq/salesConfirmationListener.js";

import * as secrets from "../secrets/secrets.js";
import * as queue from "./queue.js";

const HALF_SECOND = 500;
const HALF_MINUTE = 30000;
const CONTAINER_ENV = "container";

export async function connectRabbitMq() {
    const env = process.env.NODE_ENV;
    if (CONTAINER_ENV === env) {
        console.log("waiting for rabbitMQ to start");
        setInterval(() => {
            connectRabbitMqAndCreateQueues();
        }, HALF_MINUTE);
    } else {
        await connectRabbitMqAndCreateQueues();
    }
}

async function connectRabbitMqAndCreateQueues() {
    amqp.connect(secrets.RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        console.log("Starting rabbitMQ")
        createQueue(connection, queue.SALES_CONFIRMATION_QUEUE, queue.SALES_CONFIRMATION_ROUTING_KEY, queue.PRODUCT_TOPIC);
        createQueue(connection, queue.PRODUCT_STOCK_UPDATE_QUEUE, queue.PRODUCT_STOCK_UPDATE_ROUTING_KEY, queue.PRODUCT_TOPIC);
        console.log("Queues and Topics were defined.")
        setTimeout(function () {
            connection.close();
        }, HALF_SECOND);
    });
    listenToConfirmationQueue();
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