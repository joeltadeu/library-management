FROM openjdk:16-alpine3.13
ADD target/params-service.jar params-service.jar
EXPOSE 7502
ENTRYPOINT ["java", "-jar", "/params-service.jar"]