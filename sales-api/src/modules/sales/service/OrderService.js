import OrderRepository from "../repository/OrderRepository";
import { ProductStockUpdateSender, sendProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import * as httpStatus from "../../../config/secrets/httpStatus.js";
import * as status from "../status/OrderStatus.js";
import OrderException from "../exception/orderException.js";

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

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(httpStatus.BAD_REQUEST, "The products must be informed")
        }
    }

}

export default new OrderService();