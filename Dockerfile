FROM openjdk:11-jre-slim

COPY target/virtualpowerplant-0.0.1-SNAPSHOT.jar virtualpowerplant-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","virtualpowerplant-0.0.1-SNAPSHOT.jar"]
