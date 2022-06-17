FROM maven:3.8.5-openjdk-11-slim AS pkiBackendBuild
WORKDIR /root/src/backend
COPY . .
RUN mvn package  -DskipTests

FROM openjdk:11 AS pkiBackendRuntime
WORKDIR /app
COPY --from=pkiBackendBuild /root/src/backend/target/pki-exe.jar ./

VOLUME ./passwords
VOLUME ./keystore
COPY --from=pkiBackendBuild /root/src/backend/keystore ./keystore

EXPOSE 8080
CMD java -jar pki-exe.jar
