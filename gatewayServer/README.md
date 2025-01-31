# Spring Boot API Gateway Server

Este proyecto es un **API Gateway Server** basado en **Spring Cloud Gateway**, que actúa como un punto de entrada para los microservicios. Obtiene su configuración desde un **Config Server** y se registra en un **Eureka Server**. Además, cuenta con **Spring Security** con autenticación basada en **JWT (JSON Web Token)**.

## Requisitos
- Java 17+
- Spring Boot
- Maven
- Un Config Server en ejecución
- Un Eureka Server en ejecución
- Un servicio de autenticación que genere JWT

## Configuración del Gateway Server
### 1. Configurar `bootstrap.yml`
Para que el Gateway Server obtenga su configuración desde el Config Server, usa `bootstrap.yml`:

```
spring:
  application:
    name: gateway-server
  cloud:
    config:
      uri: http://localhost:8888
      name: gateway-server
```

Esto permite que el Gateway Server recupere su configuración desde el Config Server con el nombre `gateway-server`.

### 2. Configurar `application.yml`
El Config Server debe proporcionar las siguientes configuraciones en `gateway-server.yml`:

```
server:
  port: 8080

spring:
  cloud:
    gateway:
      routes:
        - id: service-a
          uri: lb://service-a
          predicates:
            - Path=/service-a/**
          filters:
            - AuthenticationFilter
        - id: service-b
          uri: lb://service-b
          predicates:
            - Path=/service-b/**
          filters:
            - AuthenticationFilter

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

### 3. Dependencias en `pom.xml`
Asegúrate de incluir las dependencias necesarias:

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.11.5</version>
</dependency>
```

### 4. Habilitar el Gateway Server en la Aplicación
En la clase principal, agrega la anotación:

```
@SpringBootApplication
@EnableEurekaClient
public class GatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }
}
```

### 5. Configurar Seguridad con JWT
Para interceptar y validar los tokens JWT, se crea un **filtro personalizado**:

```
@Component
public class AuthenticationFilter implements GlobalFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            Jwts.parserBuilder().setSigningKey("secret").build().parseClaimsJws(token);
        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        return chain.filter(exchange);
    }
}
```

> **Nota:** Se recomienda usar una clave secreta segura y almacenarla en variables de entorno o servicios de configuración.

## Ejecución del Gateway Server
Para ejecutar el Gateway Server, usa el siguiente comando:

```
mvn spring-boot:run
```

El Gateway Server estará disponible en:

```
http://localhost:8080
```
