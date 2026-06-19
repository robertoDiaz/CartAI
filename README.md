# Cart·AI Shopping

<img src="src/main/resources/logo/logoCartAI.png" alt="Cart·AI Shopping Logo" width="120" />

Cart·AI Shopping is a modern e-commerce backend platform built using clean coding practices, microservices design
patterns, and robust event-driven architecture.

This project is built using **Hexagonal Architecture (Ports & Adapters)** to keep business logic completely decoupled
from external frameworks, database technologies, and messaging systems.

---

## 🏛️ System Architecture

The project follows a pure Hexagonal Architecture separation, ensuring that the domain model remains free of
infrastructure concerns (like Spring annotations or database entities).

```mermaid
graph TD
    %% Primary Adapters (Incoming)
    subgraph Primary [Primary Adapters]
        RestAPI["REST Controllers (Cart, Customer, Order, Product, Auth)"]
        KafkaListeners["Kafka Listeners (CustomerAddedEventListener, OrderPlacedEventNotificationListener)"]
    end

    %% Application Layer
    subgraph Application [Application Layer]
        UseCases["Use Cases Orchestrators"]
        Commands["Command DTOs"]
    end

    %% Core Domain
    subgraph Core [Domain Layer]
        DomainEntities["Domain Entities (Order, Cart, Customer, Product, User, Role)"]
        ValueObjects["Value Objects (CustomerId, Email, ProductId, OrderId, UserId, RoleId)"]
        PortsIn["Incoming Ports (Use Case Interfaces)"]
        PortsOut["Outgoing Ports (Repository & Publisher Interfaces)"]
    end

    %% Secondary Adapters (Outgoing)
    subgraph Secondary [Secondary Adapters]
        MongoAdapters["MongoDB Adapters (MongoTemplate, Repositories)"]
        OutboxAdapters["Outbox Adapters (OutboxTransactionDocument)"]
        KafkaPublishers["Kafka Outbound Adapters (OutboxTransactionScheduler -> KafkaTemplate)"]
    end

    %% External Infrastructure
    subgraph Infrastructure [External Services]
        MongoDB[("MongoDB Database")]
        KafkaCluster["Apache Kafka Broker"]
    end

    %% Connections
    RestAPI --> Commands
    Commands --> UseCases
    UseCases -.-> PortsIn
    PortsIn --> DomainEntities
    DomainEntities --> ValueObjects
    UseCases --> PortsOut
    
    MongoAdapters -.-> PortsOut
    OutboxAdapters -.-> PortsOut
    
    MongoAdapters --> MongoDB
    OutboxAdapters --> MongoDB
    
    KafkaListeners --> UseCases
    KafkaPublishers --> KafkaCluster
    KafkaCluster --> KafkaListeners
    
    %% Outbox Scheduling
    KafkaPublishers --> MongoDB
```

---

## 🛠️ Technology Stack

- **Backend:** Spring Boot 3.5 (Java 21)
- **Security:** Spring Security & JSON Web Tokens (JWT)
- **Database:** MongoDB (Catalog, Outbox, Order, Cart, Users, and Roles storage)
- **Event Streaming:** Apache Kafka (Spring Kafka)
- **Build Tool:** Gradle
- **Boilerplate Reduction:** Lombok
- **API Documentation:** Springdoc OpenAPI / Swagger

---

## 🚀 Implemented Patterns & Best Practices

### 1. Security & Authentication (JWT + Spring Security)

* Custom security filter chains configured using Hexagonal architecture adapters.
* Authenticated endpoints using **JSON Web Tokens (JWT)**.
* Password hashing using **BCrypt** through an outgoing port.
* Sub-domain architecture support for security including roles (e.g. `User`, `Role`, `Permission`).

### 2. Transactional Outbox Pattern

To guarantee **At-Least-Once Delivery** and prevent inconsistencies between database updates and event publishing (e.g.,
publishing a message to Kafka for a database transaction that ultimately failed and rolled back), we implement the
Transactional Outbox Pattern:

* Instead of publishing events directly to Kafka in the request thread, events are saved in an `outbox_transaction`
  collection inside MongoDB within the same database transaction as the business entity.
* An
  asynchronous [OutboxTransactionScheduler](file:///Users/rober/work/CartAI/src/main/java/cart/ai/shopping/infrastructure/out/kafka/OutboxTransactionScheduler.java)
  polls MongoDB, publishes the events, and updates their status.

### 3. Distributed Locking & Concurrent Mutual Exclusion

To secure the Outbox Scheduler in clustered or multi-instance environments:

* Instead of a simple database fetch-and-update (which causes race conditions where multiple replicas dispatch the same
  message), we use an atomic `findAndModify` operation.
* This operation retrieves a pending message and updates its status to `PROCESSING` in a single atomic step at the
  database level. Other instances are prevented from pulling the same message, ensuring zero duplication at the
  scheduler level.

### 4. Kafka Resilience & Poison Pill Mitigation

* **Centralized Error Handling:** We use a global `CommonErrorHandler` configured with `DeadLetterPublishingRecoverer`
  in [KafkaErrorHandlerConfig](file:///Users/rober/work/CartAI/src/main/java/cart/ai/shopping/infrastructure/config/kafka/KafkaErrorHandlerConfig.java).
  This isolates failing payloads to a dedicated `<topic-name>.DLT` topic after 3 failed attempts (1 original + 2
  retries), avoiding partition blockage.
* **ErrorHandlingDeserializer:** Configured
  in [application.properties](file:///Users/rober/work/CartAI/src/main/resources/application.properties) to
  intercept parsing/deserialization errors immediately. This prevents invalid payloads (Poison Pills) from freezing the
  consumer thread.

### 5. End-to-End Idempotency

* **Producer Idempotency:** Enabled via `spring.kafka.producer.properties.enable.idempotence=true` to ensure network
  failures and retries between the application and the Kafka brokers do not write duplicate messages inside the topics.
* **Consumer Idempotency:** Application state validation (e.g., verifying if a shopping cart already exists before
  creating it) is implemented to handle double-delivery scenarios gracefully.

### 6. Strict Event Ordering

* To ensure all actions related to a single customer are processed in the sequence they occurred, events are partitioned
  using the `userId` as the Kafka message key (configured in the Outbox message entity), routing all customer-scoped
  events to the same partition.

---

## 📂 Project Structure

```
src/main/java/cart/ai/shopping/
├── application/                      <-- Application Layer (Orchestration & Command Pattern)
│   └── usecases/                     
│       ├── identity/                 <-- Identity Use Cases (Login, Register, User, Role management)
│       │   ├── commands/             
│       │   ├── role/                 
│       │   └── user/                 
│       └── shop/                     <-- Shop Use Cases (Cart, Customer, Order, Product)
│           ├── commands/             
│           ├── cart/                 
│           ├── customer/             
│           ├── order/                
│           └── product/              
│
├── domain/                           <-- Pure Domain Layer (Framework-free Business Logic)
│   ├── common/                       <-- Common Domain Wrappers (result/Result.java)
│   ├── model/                        
│   │   ├── identity/                 <-- Identity Domain Models (User, Role, Permission) & vos/ (Email, UserId, etc.)
│   │   └── shop/                     <-- Shop Domain Models (Cart, Customer, Order, Product) & vos/ (OrderId, etc.)
│   └── ports/                        <-- Interfaces
│       ├── common/                   <-- Common Ports (IncrementIdGeneratorPort)
│       ├── identity/                 <-- Identity Ports (Repositories, Events, PasswordEncoderPort)
│       └── shop/                     <-- Shop Ports (Repositories, Events)
│
└── infrastructure/                   <-- Infrastructure Layer (Frameworks & Adapters)
    ├── config/                       <-- Configurations (Kafka, UseCaseInjectionConfig)
    ├── in/                           <-- Primary / Inbound Adapters
    │   ├── kafka/                    <-- Kafka Event Listeners (shop events)
    │   └── rest/                     <-- REST Controllers (shop and identity controllers)
    ├── out/                          <-- Secondary / Outbound Adapters
    │   ├── kafka/                    <-- Outbox Schedulers & Publishers
    │   └── persistence/mongo/        <-- Mongo Adapters, Documents, and Mappers (common, identity, shop)
    └── security/                     <-- Security Technical Layer (configs, filters, services, adapters)
```

---

## 🔮 Future Roadmap

- [ ] **MinIO / S3 Storage Integration:**
  - Support storing and retrieving product, user, and customer images using an S3-compatible service (MinIO).
- [ ] **React Frontend Application:**
    - Build a modern user interface using React, TypeScript, and Vite.
    - Leverage HSL-tailored designs, subtle animations, and fully responsive grids for product listing, cart checkout,
      and customer dashboards.
- [ ] **Dockerization & Orchestration:**
    - Containerize the Spring Boot backend, the React frontend, MongoDB, and Apache Kafka.
    - Provide a single `docker-compose.yml` to spin up the entire local development environment instantly.
- [ ] **AWS Cloud Deployment:**
    - Set up secure infrastructure on AWS (EC2/ECS, DocumentDB for Mongo, MSK for Kafka).
    - Implement TLS/SSL security and SASL/SCRAM authentication for Apache Kafka.
- [ ] **Comprehensive Testing Suite:**
    - **Unit & Integration:** JUnit 5, Mockito, and Testcontainers to spin up isolated MongoDB/Kafka instances for
      integration testing.
    - **End-to-End (E2E) / Functional:** Write automated UI and API workflow tests using Playwright.

