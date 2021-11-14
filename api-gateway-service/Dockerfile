FROM openjdk:16-alpine3.13
ADD target/api-gateway-service.jar api-gateway-service.jar
EXPOSE 8762
ENTRYPOINT ["java", "-jar", "/api-gateway-service.jar"]