FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/order-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY orders.csv orders.csv
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]