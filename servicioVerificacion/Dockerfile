FROM openjdk:17
VOLUME /tmp
EXPOSE 0
ADD ./target/servicioVerificacion-0.0.1-SNAPSHOT.jar servicio-verificacion.jar
ENTRYPOINT ["java","-jar","/servicio-verificacion.jar"]