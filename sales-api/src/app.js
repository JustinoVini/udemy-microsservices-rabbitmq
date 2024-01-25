import express from "express";

import { connect } from "./config/db/mongoDbConfig.js";
import Order from "./modules/sales/model/Order.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connect();

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