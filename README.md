# udemy-microsservices-rabbitmq

# Inicio - criação dos containers docker

### linha de comando: 

docker run --name auth-db -p 5433:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 postgres:11

docker run --name product-db -p 5434:5432 -e POSTGRES_DB=product-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 postgres:11

docker run --name sales-db -p 27017:27017 -p 28017:28017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=123456 -d mongo

conexão

mongosh "mongodb://admin:123456@localhost:27017/"
