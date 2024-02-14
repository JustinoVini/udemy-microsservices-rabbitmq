import express from "express";
import createInitialData from "./config/db/initialData.js";
import userRoutes from "./modules/user/routes/userRoutes.js";
import tracing from "./config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

app.get("/api/status", (req, res) => {
    return res.status(200).json({
        service: 'Auth-API',
        status: "up",
        httpStatus: 200
    });
});

app.use(express.json());
app.use(tracing);
app.use(userRoutes);

// Agora, a função createInitialData() será chamada quando o servidor começar a ouvir.
app.listen(PORT, () => {
    console.log(`Server started on port: ${PORT}`);
    createInitialData();
});
