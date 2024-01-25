import mongoose from "mongoose";
import { MONGODB_URL } from "../secrets/secrets.js";

export function connect() {
    mongoose.connect(MONGODB_URL);
    mongoose.connection.on('connected', function () {
        console.log(`The application connected to mongodb successfully`);
    })
    mongoose.connection.on('error', function () {
        console.log(`The application not connected to mongodb successfully`);
    })
}