FROM eclipse-temurin:21-jdk-jammy

WORKDIR /deployments

COPY ./target/donation-backend-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENV TZ=Asia/Taipei
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /deployments/app.jar"]