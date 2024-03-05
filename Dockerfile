FROM openjdk:17-alpine
COPY ./build/libs/auth-service-1.0.jar auth-service-1.0.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","auth-service-1.0.jar"]