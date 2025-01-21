FROM openjdk:17
VOLUME /tmp
EXPOSE 0
ADD ./target/servicioConsultas-0.0.1-SNAPSHOT.jar servicio-consultas.jar
ENTRYPOINT ["java","-jar","/servicio-consultas.jar"]