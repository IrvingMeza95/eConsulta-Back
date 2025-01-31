# Spring Boot OAuth2 Authorization Server

Este proyecto es un **OAuth2 Authorization Server** basado en **Spring Security y JWT**, que proporciona autenticación y generación de tokens JWT para el ecosistema de microservicios.

## Requisitos
- Java 17+
- Spring Boot
- Maven
- Un Config Server en ejecución
- Un Eureka Server en ejecución

## Configuración del OAuth2 Server
### 1. Configurar `bootstrap.yml`
Para que el servicio obtenga su configuración desde el Config Server, usa `bootstrap.yml`:

```
spring:
  application:
    name: oauth-server
  cloud:
    config:
      uri: http://localhost:8888
      name: oauth-server
```

### 2. Configurar `application.yml`
El Config Server debe proporcionar las siguientes configuraciones en `oauth-server.yml`:

```
server:
  port: 9000

spring:
  security:
    oauth2:
      authorizationserver:
        issuer: http://localhost:9000
        client:
          client-id:
            client-secret: {noop}secret
            authorized-grant-types: password, refresh_token
            scopes: read, write

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

### 3. Dependencias en `pom.xml`
Asegúrate de incluir las dependencias necesarias:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.11.5</version>
</dependency>
```

### 4. Habilitar el Authorization Server en la Aplicación
En la clase principal, agrega la anotación:

```
@SpringBootApplication
@EnableEurekaClient
public class OauthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
```

### 5. Configurar el Authorization Server
Define la configuración del servidor OAuth2 para generar JWT:

```
@Configuration
public class AuthorizationServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>("secret-key".getBytes()));
    }
}
```

## Obtención y Refresco de Tokens JWT
Los clientes pueden obtener un **token JWT** enviando una solicitud **POST** a:

```
POST http://localhost:9000/oauth/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
client_id=client
client_secret=secret
username=admin
password=password
```

Para refrescar un **token JWT**:

```
POST http://localhost:9000/oauth/token
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token
client_id=client
client_secret=secret
refresh_token={REFRESH_TOKEN}
```

La respuesta incluirá un nuevo **token JWT** válido.

## Ejecución del OAuth2 Server
Para ejecutar el servicio, usa el siguiente comando:

```
mvn spring-boot:run
```

El OAuth2 Server estará disponible en:

```
http://localhost:9000
```

