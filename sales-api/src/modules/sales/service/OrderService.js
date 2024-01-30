import OrderRepository from "../repository/OrderRepository";
import { ProductStockUpdateSender, sendProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import * as httpStatus from "../../../config/secrets/httpStatus.js";
import * as status from "../status/OrderStatus.js";
import OrderException from "../exception/orderException.js";
import Order from "../model/Order.js";

class OrderService {

    async createOrder(req) {
        try {
            let orderData = req.body;
            this.validateOrderData(orderData);
            const { authUser } = req;
            let order = {
                status: status.PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData
            }
            await this.validateProductStock(order)
            let createdOrder = await OrderRepository.save(order);
            sendProductStockUpdateQueue(order.products);
            return {
                status: httpStatus.SUCCESS,
                createdOrder,
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message,
            }
        }
    }

    async updateOrder(orderMessage) {
        try {
            const order = JSON.parse(orderMessage);
            if (order.salesId && order.status) {
                let existingOrder = await OrderRepository.findById(order.salesId);
                if (existingOrder && order.status !== existingOrder.status) {
                    existingOrder.status = order.status;
                    await OrderRepository.save(existingOrder);
                }
            } else {
                console.log("The order message was not complete.");
            }
        } catch (error) {
            console.log(httpStatus.INTERNAL_SERVER_ERROR, "Could not parse message from queue", error);
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The products must be informed")
        }
    }

    async validateProductStock(order) {
        let stocksIsOut = true;
        if (stocksIsOut) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The stock is out for the products")
        }
    }

}

export default new OrderService();