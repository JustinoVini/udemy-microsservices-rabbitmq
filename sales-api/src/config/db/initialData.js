import Order from "../../modules/sales/model/Order.js";
import { v4 as uuidv4 } from "uuid";

export async function createInitialData() {
    try {
        let existingData = await Order.find();
        if (existingData && existingData.length > 0) {
            console.info("Remove existing data...");
            await Order.collection.drop();
        }
        await Order.create({
            products: [
                {
                    productId: 1001,
                    quantity: 2,
                },
                {
                    productId: 1002,
                    quantity: 1,
                },
                {
                    productId: 1003,
                    quantity: 1,
                },
            ],
            user: {
                id: "05c2aaae-da14-4c82-9f2a-722e3e98829c",
                name: "User test 1",
                email: "teste1@teste.com",
            },
            status: "APPROVED",
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid: uuidv4(),
            serviceid: uuidv4(),
        });
        await Order.create({
            products: [
                {
                    productId: 1001,
                    quantity: 4,
                },
                {
                    productId: 1003,
                    quantity: 2,
                },
            ],
            user: {
                id: "3a649d6e-f3ef-40c8-bb2b-36df399e2211",
                name: "User test 2",
                email: "teste2@teste.com",
            },
            status: "REJECTED",
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid: uuidv4(),
            serviceid: uuidv4(),
        });
        let initialData = await Order.find();
        console.info(
            `Initial data was created: ${JSON.stringify(initialData, undefined, 4)}`
        );
    } catch (error) {
        console.error(error);
    }
}