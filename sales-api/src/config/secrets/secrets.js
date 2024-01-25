const env = process.env;

export const MONGODB_URL = env.MONGODB_URL ? env.MONGODB_URL : "mongodb://localhost:27017/sales-db";