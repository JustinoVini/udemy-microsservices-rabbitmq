import Order from "../model/Order.js";

class OrderRepository {

    async save(order) {
        try {
            return await Order.create(order);
        } catch (error) {
            console.log(error);
            return null;
        }
    }

    async findById(id) {
        try {
            return await Order.findById(id);
        } catch (error) {
            console.log(error);
            return null;
        }
    }

    async findAll() {
        try {
            return await Order.find();
        } catch (error) {
            console.log(error);
            return null;
        }
    }

}

export default new OrderRepository();