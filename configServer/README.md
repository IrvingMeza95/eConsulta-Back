# Spring Boot Config Server

Este proyecto es un **Config Server** basado en **Spring Boot**, que permite la gestión centralizada de configuraciones para múltiples microservicios. Soporta conexión tanto a un **repositorio local** como a un **repositorio privado en Git**.

## Requisitos
- Java 17+  
- Spring Boot  
- Maven  
- Acceso a un repositorio Git privado (si se usa esta opción)  

## Configuración del Config Server
### 1. Uso con un Repositorio Local
Para utilizar un repositorio local, define la siguiente configuración en `application.yml` o `application.properties`:

```
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: file:///{ruta-al-repositorio-local}
```

Reemplaza `{ruta-al-repositorio-local}` con la ubicación de tu repositorio en el sistema de archivos.

### 2. Uso con un Repositorio Privado en Git
Si deseas conectarte a un repositorio privado en GitHub o GitLab, configura lo siguiente:

```
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/usuario/repositorio-config
          username: {tu-usuario}
          password: {tu-token-de-acceso}
```

> **Nota:** Es recomendable usar variables de entorno o un servicio seguro para manejar credenciales en lugar de definirlas en texto plano.

## Ejecución del Config Server
Para ejecutar el servidor, usa el siguiente comando:

```
mvn spring-boot:run
```

## Acceso a la Configuración desde un Cliente
Los microservicios pueden acceder a su configuración con la siguiente URL:

```
http://localhost:8888/{nombre-del-servicio}/{perfil}
```

Ejemplo para un servicio llamado `orders` con perfil `dev`:

```
http://localhost:8888/orders/dev
```

