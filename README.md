# Library Management using Spring Cloud, Eureka, Zuul, Zipkin, Sleuth


## Architecture:
![Alt text](assets/library-management-architecture.svg?raw=true "Library Management Architecture")

## There are three microservices:

- **Books** : This microservice is responsible for managing books. A user can create, update, get details, list and delete books.
- **Orders** : This microservice is responsible for managing orders. A member can borrow a book and return it.
- **Params** : This microservice is responsible for managing params. These params are used to loan a book

### Business rules ###
- No need to authenticate the user (we can assume they are members and are already authenticated)
- Maximum number of books loaned at any time is 3 per user
- If a member has any outstanding loaned books, they cannot loan any more until all books returned.
- There is only 1 copy of each book
- Each book can have more than one category 

### EndPoints ###

| Service       | EndPoint                     | Port    | Method | Description                                      |
| ------------- | -----------------------------| :-----: | :-----:| ------------------------------------------------ |
| Books         | /api/v1/books/{id}           | 7500    | GET    | Return detail of specified book                  |
| Books         | /api/v1/books                | 7500    | GET    | Return details of all books                      |
| Books         | /api/v1/books                | 7500    | POST   | Insert a new book                                |
| Books         | /api/v1/books/{id}           | 7500    | PUT    | Update a specific book                           |
| Books         | /api/v1/books/{id}           | 7500    | DELETE | Delete a specific book                           |
| Orders        | /api/v1/loan                 | 7501    | POST   | Loan a book                                      |
| Orders        | /api/v1/return               | 7501    | POST   | Return a book                                    |
| Params        | /api/v1/params/by-key/{key}  | 7502    | GET    | Return param by key                              |

### Gateways ###

| Service       | EndPoint                                  |
| ------------- | :---------------------------------------: |
| Books         | **/book**/api/v1/books/{id}               | 
| Books         | **/book**/api/v1/books                    |
| Orders        | **/order**/api/v1/loan                    |
| Orders        | **/order**/api/v1/return                  |
| Params        | **/param**/api/v1/params/by-key/{key}     |

URI for gateway : *http://localhost:8762*

### Documentation and examples ###

###Swagger

- **Books** : http://localhost:8762/book/swagger-ui.html
- **Orders** : http://localhost:8762/order/swagger-ui.html
- **Params** : http://localhost:8762/param/swagger-ui.html

###Postman collection

![Alt text](assets/postman-collection-folder.png?raw=true "Postman collection folder")
## Used Netflix OSS:

- **Netflix Eureka** is used for discovery service.
- **Netflix Zuul** is used for gateway.

## Distributed Tracing:

- **Sleuth** and **Zipkin**

You can open Zipkin : http://localhost:9411

## Used ELK STACK:

- **ElasticSearch** is on 6972 port
- **Logstash TCP** is on 5000 port
- **Kibana** is on 5601 port

Open kibana with http://localhost:5601/. You must define an index pattern (taner-*) on Management/Stack Management.

## Grafana:

You can open Grafana : http://localhost:3000/

username/password: admin

## Build & Run

- *>mvn clean package* : to build
- *>docker-compose up* --build : build docker images and containers and run containers
- *>docker-compose stop* : stop the dockerized services
- Each maven module has a Dockerfile.

In docker-compose.yml file:

- Books Service : **__7500__** port is mapped to **__7500__** port of host
- Orders Service : **__7501__** port is mapped to **__7501__** port of host
- Params Service : **__7502__** port is mapped to **__7502__** port of host
- Eureka Discovery Service : **__8761__** port is mapped to **__8761__** port of host
- Spring Boot (/ Zuul) Gateway Service : **__8762__** port is mapped to **__8762__** port of host 

## VERSIONS

### 1.0.0

- Sleuth and Zipkin Integration
- ElasticSearch, Kibana, Logstash integration
- Spring Boot Gateway
- Spring-Boot 2.5.6.RELEASE
- Java 16