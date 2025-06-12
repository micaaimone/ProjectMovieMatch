# 🎥 MovieMatch API

**MovieMatch** es una API desarrollada en Java con Spring Boot que permite a los usuarios:
- Crear amistades y grupos.
- Recibir recomendaciones personalizadas de películas y series basadas en sus gustos y edad.
- Calificar y reseñar contenidos.
- Gestionar listas personalizadas.
- Acceder a coincidencias de gustos dentro de chats individuales o grupales.
- Disfrutar de funcionalidades premium a través de suscripciones con MercadoPago.

---

## 🚀 Tecnologías y Frameworks

| Tecnología | Descripción |
|:------------|:--------------|
| **Java 21** | Lenguaje de programación principal. |
| **Spring Boot 3.4.5** | Framework de backend. |
| **Maven** | Herramienta de gestión de dependencias y build. |
| **JPA (Hibernate)** | ORM para persistencia en base de datos relacional. |
| **MySQL** | Motor de base de datos. |
| **JWT Security** | Autenticación basada en tokens. |
| **Swagger UI (Springdoc OpenAPI)** | Documentación interactiva de endpoints. |

---

## 📦 Dependencias y Librerías

| Librería                      | Versión   | Uso |
|:------------------------------|:-----------|:----------------|
| **spring-boot-starter-data-jpa** | 3.4.5 | Persistencia con Hibernate. |
| **spring-boot-starter-web** | 3.4.5 | Creación de controladores REST. |
| **spring-boot-starter-security** | 3.4.5 | Seguridad con JWT. |
| **MySQL Connector/J**          | runtime  | Conexión a base de datos MySQL. |
| **Lombok**                     | 1.18.34  | Anotaciones para eliminar código repetitivo. |
| **Dotenv-Java**                | 3.0.0    | Lectura de variables desde `.env`. |
| **ModelMapper**                | 3.1.1    | Conversión entre entidades y DTOs. |
| **MercadoPago SDK Java**       | 2.4.0    | Integración de pagos premium. |
| **JWT (io.jsonwebtoken)**      | 0.11.5   | Creación y validación de tokens. |
| **Springdoc OpenAPI**          | 2.8.8    | Generación de documentación Swagger. |
| **spring-boot-starter-validation** | 3.4.5 | Validaciones automáticas en DTOs y entidades. |

---

## 🌐 APIs Externas Utilizadas

| API              | Uso en MovieMatch |
|:-----------------|:-----------------|
| **MercadoPago**   | Gestión de pagos para suscripciones premium. |
| **OpenAPI (Swagger UI)** | Documentación interactiva de los endpoints REST. |

---

## 📊 Base de Datos

- Motor: **MySQL**
- ORM: **Hibernate (JPA)**
- Configuración vía `application.properties` y variables desde `.env`.

---

## 📄 Documentación de Endpoints

Accesible desde:
http://localhost:8080/swagger-ui/index.html


---

## 📝 Instalación y Ejecución

1. Clonar el repositorio.
2. Crear un archivo `.env` con las variables de entorno necesarias (puerto, credenciales de base de datos, claves JWT, MercadoPago, etc).
3. Configurar `application.properties` para usar las variables del `.env`.
4. Levantar MySQL y crear la base de datos.
5. Ejecutar.

