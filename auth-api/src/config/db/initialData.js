import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

async function createInitialData() {
    console.log("Trying to sync User model...");
    try {
        await User.sync({ force: true });
        const password = await bcrypt.hash("123456", 10);

        await User.create({
            name: "User test 1",
            email: "teste1@teste.com",
            password: password,
        });

        await User.create({
            name: "User test 2",
            email: "teste2@teste.com",
            password: password,
        });

        console.log("Data creation successfully!");
    } catch (err) {
        console.error("Error in initial data creation:", err);
    }
}

export default createInitialData;
