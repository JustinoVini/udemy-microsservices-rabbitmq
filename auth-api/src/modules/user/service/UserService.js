import userRepository from "../repository/userRepository";
import * as httpStatus from "../../../config/constants/httpStatus"

class UserService {

    async findByEmail(req) {
        try {
            const { email } = req.params;
            this.validarDadosDaRequisicao(email);
            let user = userRepository.findByEmail(email);

            if (!user) {

            }
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    nome: user.nome,
                    email: user.email,
                }
            }

        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.status,
            }
        }
    }

    validadeRequestData(email) {
        if (!email) {
            throw new Error('User email was not informed.')
        }
    }

}

export default new UserService();