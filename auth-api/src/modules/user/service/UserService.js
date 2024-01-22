import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

import userRepository from "../repository/userRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js"
import UserException from "../exception/UserException.js";
import * as secrets from "../../../config/constants/secrets.js"

class UserService {

    async findByEmail(req) {
        try {
            const { email } = req.params;
            this.validateRequestData(email);
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);
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

    validateRequestData(email) {
        if (!email) {
            throw new UserException(httpStatus.BAD_REQUEST, 'User email was not informed.')
        }
    }

    validateUserNotFound(user) {
        if (!user) {
            throw new UserException(httpStatus.BAD_REQUEST, 'User was not found.')
        }
    }

    validateAccessTokenData(email, password) {
        if (!email || !password) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Email and Password must be informed")
        }
    }

    async validatePassword(password, hashPassword) {
        if (!await bcrypt.compare(password, hashPassword)) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match.")
        }
    }

    async getAccessToken(req) {
        try {
            const { email, password } = req.body;
            this.validateAccessTokenData(email, password);
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);
            await this.validatePassword(password, user.password);
            const authUser = { id: user.id, name: user.name, email: user.email }
            const accessToken = jwt.sign({ authUser }, secrets.API_SECRET, { expiresIn: '1d' })
            return {
                status: httpStatus.SUCCESS,
                accessToken,
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message,
            }
        }
    }

}

export default new UserService();