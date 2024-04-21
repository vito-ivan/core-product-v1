FROM 412145464225.dkr.ecr.us-east-1.amazonaws.com/dependencies-ifm:maven-3.8.6-18-slim AS builder
WORKDIR /opt/app
COPY . .
RUN mvn -e clean verify
FROM 412145464225.dkr.ecr.us-east-1.amazonaws.com/dependencies-ifm:java-18-slim
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar ./
EXPOSE 80
COPY /src/main/resources/application-prod.properties /opt/app/application-prod.properties
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /opt/app/*.jar --spring.config.location=/opt/app/application-prod.properties