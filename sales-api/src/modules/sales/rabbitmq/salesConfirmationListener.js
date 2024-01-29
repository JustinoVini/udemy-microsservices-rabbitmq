import amqp from "amqplib/callback_api.js";

import * as secrets from "../../../config/secrets/secrets.js";
import * as queue from "../../../config/rabbitmq/queue.js";

export function listenToConfirmationQueue() {
    amqp.connect(secrets.RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        console.log("Listening to Sales confirmation Queue...")
        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }
            channel.consume(queue.SALES_CONFIRMATION_QUEUE, (message) => {
                console.log(`Receiving message from queue: ${message.content.toString()}`);
            }, {
                noAck: true
            })
        })
    });
}