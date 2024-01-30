import express from "express";

import { connectMongoDb } from "./config/db/mongoDbConfig.js";
import { createInitialData } from "./config/db/initialData.js";
import Order from "./modules/sales/model/Order.js";
import checkToken from "./config/auth/checkToken.js";
import { connectRabbitMq } from "./config/rabbitmq/rabbitConfig.js";
import { sendProductStockUpdateQueue } from "./modules/product/rabbitmq/productStockUpdateSender.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
connectRabbitMq();
createInitialData();

app.use(checkToken);

/* app.get("/teste", (req, res) => {
    try {
        sendProductStockUpdateQueue([
            {
                productId: 1001,
                quantity: 3,
            },
            {
                productId: 1002,
                quantity: 2,
            },
            {
                productId: 1003,
                quantity: 1,
            }
        ])
        return res.status(200).json({ status: 200 })
    } catch (error) {
        console.log(error);
        return res.status(500).json({ error: true })
    }
}) */

app.get("/api/status", async (req, res) => {
    let teste = await Order.find();
    console.log(teste);
    return res.status(200).json({
        service: 'Sales-API',
        status: "up",
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.log(`Server started on port: ${PORT}`);
})