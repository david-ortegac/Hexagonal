# Arquitectura y Estándares para Microservicios Java (Spring Boot + AWS Lambda)

## Stack Tecnológico

- **Java 21**
- **Spring Boot 4.x** con Gradle
- **Spring Data JPA** + **MySQL** (`mysql-connector-j`)
- **Lombok** para reducir boilerplate
- **MapStruct 1.6.x** para mapeo entre capas
- **AWS Serverless Java Container** (`aws-serverless-java-container-springboot4`) para despliegue en AWS Lambda
- **JUnit Platform** para pruebas

### Dependencias clave en `build.gradle`

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'com.amazonaws.serverless:aws-serverless-java-container-springboot4:3.0.1'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    implementation 'org.mapstruct:mapstruct:1.6.3'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
}
```

### Configuración del JAR para Lambda

```groovy
tasks.named('jar') { enabled = true }
tasks.named('bootJar') { enabled = false }

tasks.register('buildLambdaZip', Zip) {
    dependsOn tasks.named('classes')
    archiveFileName = 'ms-{nombre}-lambda.zip'
    destinationDirectory = layout.buildDirectory.dir('distributions')
    from(sourceSets.main.output)
    into('lib') { from(configurations.runtimeClasspath) }
}
```

---

## Arquitectura en Capas (Hexagonal / Ports & Adapters)

El proyecto sigue una arquitectura hexagonal con cuatro capas bien definidas. La estructura de paquetes refleja directamente esta separación.

```
co.edu.cun.ms_backend_java_{dominio}.{dominio}/
├── adapter/
│   └── restfull/
│       └── v1/
│           └── controller/
│               ├── DTO/           ← DTOs de entrada/salida HTTP
│               ├── Mappers/       ← Mapper Adapter ↔ Domain
│               └── {Entidad}Controller.java
├── application/
│   └── I{Entidad}.java            ← Puerto (interfaz de casos de uso)
├── domain/
│   ├── DTO/
│   │   └── {Entidad}Domain.java   ← DTO de dominio
│   └── {Entidad}Impl.java         ← Implementación de la lógica de negocio
├── infraestructure/
│   ├── Entity/
│   │   └── {Entidad}.java         ← Entidad JPA
│   ├── Mapper/
│   │   └── I{Entidad}Mapper.java  ← Mapper Entity ↔ Domain
│   └── Respository/
│       └── I{Entidad}Repository.java ← Repositorio JPA
└── lambda/
    └── StreamLambdaHandler.java   ← Handler AWS Lambda
```

### Flujo de datos entre capas

```
HTTP Request
    ↓
Controller (adapter)
    ↓ usa IMapperAdapter → convierte PersonAdapter → PersonDomain
    ↓ llama a IPerson (puerto)
        ↓
    PersonImpl (domain) — implementa IPerson
        ↓ usa IPersonMapper → convierte PersonDomain ↔ Person (Entity)
        ↓ llama a IPersonRepository
            ↓
        Base de datos (MySQL)
```

---

## Nomenclaturas y Convenciones

### Paquetes

| Capa           | Paquete                                      |
|----------------|----------------------------------------------|
| Adapter REST   | `adapter.restfull.v1.controller`             |
| DTOs adapter   | `adapter.restfull.v1.controller.DTO`         |
| Mappers adapter| `adapter.restfull.v1.controller.Mappers`     |
| Application    | `application`                                |
| Domain         | `domain`                                     |
| Domain DTOs    | `domain.DTO`                                 |
| Infrastructure | `infraestructure`                            |
| Entities       | `infraestructure.Entity`                     |
| Infra Mappers  | `infraestructure.Mapper`                     |
| Repository     | `infraestructure.Respository`                |
| Lambda         | `lambda`                                     |

> Nota: el paquete `infraestructure` mantiene esa ortografía (sin la 'r' doble) por consistencia con el proyecto base.

### Clases e Interfaces

| Artefacto                  | Patrón de nombre              | Ejemplo                  |
|----------------------------|-------------------------------|--------------------------|
| DTO HTTP (adapter)         | `{Entidad}Adapter`            | `PersonAdapter`          |
| DTO de dominio             | `{Entidad}Domain`             | `PersonDomain`           |
| Entidad JPA                | `{Entidad}`                   | `Person`                 |
| Puerto (interfaz app)      | `I{Entidad}`                  | `IPerson`                |
| Implementación de dominio  | `{Entidad}Impl`               | `PersonImpl`             |
| Mapper adapter ↔ domain    | `IMapperAdapter`              | `IMapperAdapter`         |
| Mapper entity ↔ domain     | `I{Entidad}Mapper`            | `IPersonMapper`          |
| Repositorio JPA            | `I{Entidad}Repository`        | `IPersonRepository`      |
| Controlador REST           | `{Entidad}Controller`         | `PersonaController`      |
| Handler Lambda             | `StreamLambdaHandler`         | `StreamLambdaHandler`    |

### Rutas REST

- Base path: `/api/v1/{recurso-en-plural}`
- Ejemplo: `/api/v1/persons`
- Versión siempre en la URL: `v1`, `v2`, etc.

### Métodos del puerto de aplicación (`I{Entidad}`)

```java
List<{Entidad}Domain> getAll{Entidades}();
{Entidad}Domain get{Entidad}ById(int id);
{Entidad}Domain create{Entidad}({Entidad}Domain entity);
{Entidad}Domain update{Entidad}({Entidad}Domain entity, int id);
String delete{Entidad}(int id);
```

### Métodos de los Mappers (MapStruct)

**IMapperAdapter** (adapter ↔ domain):
```java
{Entidad}Adapter toAdapter({Entidad}Domain domain);
{Entidad}Domain toDomain({Entidad}Adapter adapter);
List<{Entidad}Adapter> toAdapterList(List<{Entidad}Domain> domains);
```

**I{Entidad}Mapper** (entity ↔ domain):
```java
{Entidad}Domain toDomain({Entidad} entity);
{Entidad} toEntity({Entidad}Domain domain);
List<{Entidad}Domain> toDomainList(List<{Entidad}> entities);
```

---

## Patrones de Implementación

### Entidad JPA

- Usar `@Data` de Lombok
- Timestamps `createdAt` y `updatedAt` como `String` con formato `"yyyy-MM-dd HH:mm:ss"`
- Usar `@PrePersist` y `@PreUpdate` para gestión automática de fechas
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` para IDs

```java
@Entity
@Table(name = "{tabla}")
@Data
public class {Entidad} {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ... campos con @Column(name = "campo", nullable = false)

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @PrePersist
    void onCreate() {
        final String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        if (createdAt == null || createdAt.isBlank()) createdAt = now;
        if (updatedAt == null || updatedAt.isBlank()) updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}
```

### Implementación de dominio (`{Entidad}Impl`)

- Anotada con `@Service`
- Inyección por constructor (sin `@Autowired`)
- Operaciones de escritura con `@Transactional`
- Lanzar `RuntimeException` cuando no se encuentra un recurso: `throw new RuntimeException("{Entidad} not found")`

```java
@Service
public class {Entidad}Impl implements I{Entidad} {
    private final I{Entidad}Repository repository;
    private final I{Entidad}Mapper mapper;

    public {Entidad}Impl(I{Entidad}Repository repository, I{Entidad}Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    // ...
}
```

### Controlador REST

- Inyección por constructor
- Retornar siempre `ResponseEntity<T>`
- Usar `ResponseEntity.ok().body(...)` para respuestas exitosas

```java
@RestController
@RequestMapping("/api/v1/{recurso}")
public class {Entidad}Controller {
    private final I{Entidad} {entidad}Service;
    private final IMapperAdapter mapperAdapter;

    public {Entidad}Controller(I{Entidad} {entidad}Service, IMapperAdapter mapperAdapter) {
        this.{entidad}Service = {entidad}Service;
        this.mapperAdapter = mapperAdapter;
    }
}
```

### Handler AWS Lambda

Siempre el mismo patrón, solo cambia la clase `Application`:

```java
public class StreamLambdaHandler implements RequestStreamHandler {
    private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler({Dominio}Application.class);
            final String stageName = System.getenv("API_STAGE_NAME");
            if (stageName != null && !stageName.isBlank()) {
                handler.stripBasePath("/" + stageName);
            }
        } catch (ContainerInitializationException exception) {
            throw new RuntimeException("No se pudo inicializar Spring Boot para AWS Lambda", exception);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
```

---

## Configuración `application.properties`

Todas las propiedades sensibles se externalizan con variables de entorno y valores por defecto para desarrollo local:

```properties
spring.application.name=backend-java-api-{dominio}
server.port=8080

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/{db_name}}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:rootpassword}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:none}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:true}
spring.jpa.open-in-view=false

# Hikari optimizado para Lambda (conexiones mínimas)
spring.datasource.hikari.maximum-pool-size=${SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE:1}
spring.datasource.hikari.minimum-idle=${SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE:0}
spring.datasource.hikari.connection-timeout=${SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT:30000}
spring.datasource.hikari.idle-timeout=${SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT:10000}
spring.datasource.hikari.max-lifetime=${SPRING_DATASOURCE_HIKARI_MAX_LIFETIME:30000}
```

> El pool de Hikari se configura con `maximum-pool-size=1` porque en Lambda cada instancia maneja una sola solicitud concurrente.

---

## Nombre del artefacto y grupo Maven

- **Group ID**: `co.edu.cun.ms_backend_java_{dominio}`
- **Artifact ID / rootProject.name**: nombre del dominio en minúsculas
- **Nombre del ZIP Lambda**: `ms-backend-java-{dominio}-lambda.zip`
