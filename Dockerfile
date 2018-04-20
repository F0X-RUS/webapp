FROM openjdk:8
ADD target/webapp-0.1.0.jar webapp-0.1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "webapp-0.1.0.jar"]