FROM openjdk:17
COPY target/noteapp-0.0.1-SNAPSHOT.jar noteapp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/noteapp-0.0.1-SNAPSHOT.jar"]