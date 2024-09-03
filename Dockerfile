FROM openjdk:17-oracle
LABEL authors="glebegunov"

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve
COPY src src
RUN ./mvnw package
EXPOSE 8000

ENTRYPOINT ["java", "-jar"]
CMD ["target/simple-crud-*.jar"]