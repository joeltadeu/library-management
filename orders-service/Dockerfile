FROM openjdk:16-alpine3.13
ADD target/orders-service.jar orders-service.jar
EXPOSE 7501
ENTRYPOINT ["java", "-jar", "/orders-service.jar"]
