FROM openjdk:17
VOLUME /tmp
ADD ./target/servicioOauth-0.0.1-SNAPSHOT.jar servicio-Oauth.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","/servicio-Oauth.jar"]