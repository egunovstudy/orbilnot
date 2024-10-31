FROM openjdk:17-oracle
LABEL authors="glebegunov"

COPY migration/pom.xml ./mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve
COPY migration/sql sql

ENTRYPOINT ["./mvnw"]
CMD ["liquibase:update"]