# ms_backend_java_persons

Guia rapida para ejecutar el servicio en local y desplegarlo en AWS Lambda (Java 21 + API Gateway HTTP API v2).

## Requisitos

- Java 21
- Gradle Wrapper (`./gradlew`)
- Base de datos MySQL accesible desde local y/o Lambda

## Ejecutar en local

1. Configura variables de entorno (recomendado):

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/prueba'
export SPRING_DATASOURCE_USERNAME='root'
export SPRING_DATASOURCE_PASSWORD='rootpassword'
export SPRING_JPA_HIBERNATE_DDL_AUTO='update'
export SPRING_JPA_SHOW_SQL='true'
```

2. Ejecuta la aplicacion:

```bash
./gradlew bootRun
```

3. Prueba endpoint:

```bash
curl -X GET "http://localhost:8080/api/v1/persons"
```

## Build para AWS Lambda

Este proyecto usa un empaquetado ZIP compatible con Lambda:

```bash
./gradlew clean buildLambdaZip
```

Artefacto generado:

- `build/distributions/ms-backend-java-persons-lambda.zip`

## Configuracion en AWS Lambda

- Runtime: `Java 21`
- Handler: `co.edu.cun.ms_backend_java_persons.persons.lambda.StreamLambdaHandler::handleRequest`
- Arquitectura recomendada: `arm64`
- Memoria recomendada: `1024` o `1536`
- Timeout recomendado: `30` a `60` segundos

### Variables de entorno en Lambda

Minimas requeridas:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `API_STAGE_NAME=prod` (o el stage real de API Gateway)

Recomendadas para Lambda:

- `SPRING_PROFILES_ACTIVE=lambda`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=none`
- `SPRING_JPA_SHOW_SQL=false`
- `SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=1`
- `SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=0`

## API Gateway (HTTP API v2)

Configura integracion Lambda Proxy con stage `prod` (o el que uses).

URL esperada de ejemplo:

- `GET https://<api-id>.execute-api.us-east-1.amazonaws.com/prod/api/v1/persons`

Nota: no usar `/{proxy+}` literal en la URL.

## Endpoints principales

- `GET /api/v1/persons`
- `GET /api/v1/persons/{id}`
- `POST /api/v1/persons`
- `PUT /api/v1/persons/{id}`
- `DELETE /api/v1/persons/{id}`

## Ejemplo POST

```bash
curl -X POST "https://<api-id>.execute-api.us-east-1.amazonaws.com/prod/api/v1/persons" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Perez",
    "email": "juan.perez@example.com",
    "phone": "3001112233",
    "address": "Calle 10 #20-30",
    "city": "Bogota"
  }'
```

## Referencias

- [aws-serverless-java-container](https://github.com/aws/serverless-java-container)
- [Quick start Spring Boot 4](https://github.com/aws/serverless-java-container/wiki/Quick-start---Spring-Boot4)

