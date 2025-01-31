# Spring Boot Eureka Server

Este proyecto es un **Eureka Server** basado en **Spring Boot**, que actúa como un servicio de descubrimiento para microservicios. Obtiene su configuración de un **Config Server**.

## Requisitos
- Java 17+
- Spring Boot
- Maven
- Un Config Server en ejecución

## Configuración del Eureka Server
### 1. Configurar `bootstrap.yml`
Para que el Eureka Server obtenga su configuración desde el Config Server, usa `bootstrap.yml`:

```
spring:
  application:
    name: eureka-server
  cloud:
    config:
      uri: http://localhost:8888
      name: eureka-server
```

Esto permite que el Eureka Server recupere su configuración desde el Config Server con el nombre `eureka-server`.

### 2. Configurar `application.yml`
El Config Server debe proporcionar las siguientes configuraciones en `eureka-server.yml`:

```
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

### 3. Dependencias en `pom.xml`
Asegúrate de incluir la dependencia de Eureka Server:

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### 4. Habilitar Eureka Server en la Aplicación
En la clase principal, agrega la anotación:

```
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

## Ejecución del Eureka Server
Para ejecutar el Eureka Server, usa el siguiente comando:

```
mvn spring-boot:run
```

El Eureka Server estará disponible en:

```
http://localhost:8761
```
