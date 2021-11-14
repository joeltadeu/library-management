FROM openjdk:16-alpine3.13
ADD target/books-service.jar books-service.jar
EXPOSE 7500
ENTRYPOINT ["java", "-jar", "/books-service.jar"]