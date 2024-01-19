import User from "../model/User";

class UserRepository {

    // busca pelo id
    async findById(id) {
        try {
            return await User.findOne({
                where: {
                    id
                }
            })
        } catch (error) {
            console.error(error.message)
        }
    }

    // busca pelo email
    async findByEmail(email) {
        try {
            return await User.findOne({
                where: {
                    email
                }
            })
        } catch (error) {
            console.error(error.message)
        }
    }

}

export default new UserRepository();