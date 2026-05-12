FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/catalog-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY catalog.csv catalog.csv
EXPOSE 4567
ENTRYPOINT ["java", "-jar", "app.jar"]