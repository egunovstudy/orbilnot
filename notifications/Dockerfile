# pre-fetch dependencies
FROM maven:3.8.5-openjdk-17 AS dependencies
WORKDIR /opt/app
COPY ./billing/pom.xml billing/pom.xml
COPY ./order/pom.xml order/pom.xml
COPY ./notifications/pom.xml notifications/pom.xml
COPY ./kitchen/pom.xml kitchen/pom.xml
COPY ./foodstuff/pom.xml foodstuff/pom.xml
COPY ./gateway/pom.xml gateway/pom.xml
COPY model/pom.xml model/pom.xml
COPY pom.xml .
RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=model

# build the jar
FROM maven:3.8.5-openjdk-17 AS BUILDER
WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY billing/src /opt/app/billing/src
COPY model/src /opt/app/model/src
COPY order/src /opt/app/order/src
COPY foodstuff/src /opt/app/foodstuff/src
COPY kitchen/src /opt/app/kitchen/src
COPY notifications/src /opt/app/notifications/src
COPY gateway/src /opt/app/gateway/src
RUN mvn -B -e install -DskipTests


# prepeare runtime env
FROM openjdk:17-slim
WORKDIR /opt/app
COPY --from=builder /opt/app/notifications/target/*.jar /app.jar
EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app.jar"]