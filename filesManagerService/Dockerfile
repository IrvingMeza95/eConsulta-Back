FROM openjdk:17
VOLUME /tmp
EXPOSE 0
ADD ./target/filesManagerService-0.0.1-SNAPSHOT.jar filesManagerService.jar
ENTRYPOINT ["java","-jar","/filesManagerService.jar"]