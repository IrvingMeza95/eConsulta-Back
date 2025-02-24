![Logo de la App](./full_logo.png)

# Proyecto eConsultas: Gestión de Consultas Médicas

eConsultas es una aplicación web diseñada para gestionar de manera integral las operaciones de una clínica médica. 
El sistema permite administrar servicios médicos, paquetes de servicios, registros de pacientes, médicos y consultas, 
además de gestionar facturación y generar reportes de ganancias en tiempo real. La aplicación se desarrolló siguiendo las consignas del concurso, 
donde podemos destacar la implementación de un entorno real de trabajo, con todas las operaciones ABML (Altas, Bajas, Modificaciones y Lecturas) 
necesarias, solicitudes a la API bien manejadas y manejo de cookies/Local Storage correctos.

## Proyecto Spring Boot eConsulta Dockerizado

Este proyecto es una aplicación Java basada en Spring Boot, la cual está dockerizada y se despliega utilizando `docker-compose`. 
Utiliza imágenes públicas de Docker Hub para su ejecución.

## HackaCode 2025

### Descripción del concurso

Este proyecto está participando en **HackaCode 2025**, un concurso dirigido a desarrolladores semisenior, junior y trainees. 
Su objetivo es brindar la oportunidad a nuevos desarrolladores de adquirir experiencia práctica en el mundo del desarrollo y 
el trabajo en equipo, replicando un entorno lo más parecido posible al mundo real.

### Participantes del equipo

- Irving Meza (Backend)
    - [GitHub](https://github.com/IrvingMeza95)
    - [Linkedin](https://www.linkedin.com/in/irving-meza/)
- Francisco Carrizo (Fullstack)
    - [Github](https://github.com/FrancarriYT)
    - [Linkedin](https://www.linkedin.com/in/francisco-carrizo-4016ab25b/)
- Martín Sosa (Frontend)
    - [Github](https://github.com/martinsosafer)
    - [Linkedin](https://www.linkedin.com/in/mart%C3%ADn-fernandez-53917b245/)

## ⚙️ Gestión de Configuración

Este proyecto utiliza un **servidor de configuración centralizado** basado en **Spring Cloud Config Server**, el cual se encarga de gestionar 
las propiedades y parámetros de todos los microservicios de forma externa.

### 📌 Repositorio de Configuración
Toda la configuración se encuentra almacenada en el siguiente repositorio:

🔗 [eConsulta-Back-Configs](https://github.com/IrvingMeza95/eConsulta-Back-Configs.git)

Los microservicios obtienen su configuración desde este servidor, lo que permite:
- ✅ Mantener la configuración separada del código.
- ✅ Aplicar cambios en caliente sin necesidad de redeploys.
- ✅ Centralizar y versionar las propiedades del sistema.

### 🔧 Configuración en los Microservicios
Cada microservicio está configurado para obtener sus propiedades desde el servidor de configuración mediante la siguiente URL:

```plaintext
http://config-server:8888/{application}/{profile}
```

Donde:
- `{application}` es el nombre del microservicio.
- `{profile}` es el entorno de configuración (`dev`, `qa`, `prod`, etc.).

Por ejemplo, para el servicio `servicioConsultas` en entorno de desarrollo:

```plaintext
http://config-server:8888/servicioConsultas/dev
```

### 🚀 Cómo Ejecutarlo
El servidor de configuración se despliega junto con los demás microservicios en Docker Compose. Para más detalles sobre cómo iniciar el sistema, revisa el repositorio correspondiente:

🔗 [Repositorio Docker-Compose](https://github.com/tu-org/docker-compose-repo)

Ahí encontrarás instrucciones sobre cómo iniciar los servicios y sus configuraciones específicas.