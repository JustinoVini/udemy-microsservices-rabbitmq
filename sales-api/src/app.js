import express from "express";

import { connect } from "./config/db/mongoDbConfig.js";
import { createInitialData } from "./config/db/initialData.js";
import Order from "./modules/sales/model/Order.js";
import checkToken from "./config/auth/checkToken.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connect();
createInitialData();

app.use(checkToken);

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