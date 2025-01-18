FROM openjdk:17
VOLUME /tmp
EXPOSE 0
ADD ./target/servicioUsuarios-0.0.1-SNAPSHOT.jar servicio-usuarios.jar
ENTRYPOINT ["java","-jar","/servicio-usuarios.jar"]