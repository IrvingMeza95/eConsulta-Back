FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD ./target/gatewayServer-0.0.1-SNAPSHOT.jar gateway-server.jar
ENTRYPOINT ["java","-jar","/gateway-server.jar"]