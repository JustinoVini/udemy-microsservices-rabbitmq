import express from "express";
import createInitialData from "./config/db/initialData.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

// Chame a função diretamente
createInitialData();

app.get("/api/status", (req, res) => {
    return res.status(200).json({
        service: 'Auth-API',
        status: "up",
        httpStatus: 200
    });
});

app.listen(PORT, () => {
    console.log(`Server started on port: ${PORT}`);
});
