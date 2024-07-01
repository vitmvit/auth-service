FROM openjdk:17-alpine
COPY ./build/libs/auth-service-1.0.jar auth-service-1.0.jar
ENTRYPOINT ["java","-jar", "auth-service-1.0.jar"]