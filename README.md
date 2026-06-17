# Bikemmerce

Bikemmerce is a personal e-commerce project designed to demonstrate clean coding practices, modern Java development, and
scalable system design. The project is focused on selling bicycles and related sports equipment.

This project is built using **Hexagonal Architecture (Ports & Adapters)** to keep the core business logic completely
separated from external frameworks, databases, and message queues.

> [!NOTE]
> This is a work in progress and is continuously evolving. More features and integrations are being added incrementally.

---

## 🛠️ Technology Stack

- **Backend:** Spring Boot 3.5 (Java 21)
- **Database:** MongoDB (catalog, orders, and cart storage)
- **Event Streaming:** Apache Kafka (scheduled for integration)
- **Build Tool:** Gradle
- **Boilerplate Reduction:** Lombok
- **API Documentation:** Springdoc OpenAPI / Swagger

---

## 🏛️ Architectural Overview

This project follows **Hexagonal Architecture (Ports and Adapters)**, dividing the codebase into three main layers:

1. **Domain:** The core business rules. Totally free of any framework dependencies (Spring, MongoDB, etc.). Contains
   entities, value objects (Records), domain results, and repository ports (interfaces).
2. **Application (Use Cases):** Orchestrates the flow of data to and from the domain. Contains the execution logic for
   business actions. Uses the **Command Pattern** (under `commands/` package) to encapsulate and validate inputs from
   the outer REST layer, separating DTOs from domain model instantiation.
3. **Infrastructure (Adapters & Frameworks):** Implements the ports defined by the domain and provides framework
   configuration. Divided into:
    - **Incoming (Primary) Adapters (`infrastructure/in/rest`):** REST controllers (`/api/products`, `/api/customers`,
      etc.) mapping incoming REST requests to Application Commands.
    - **Outgoing (Secondary) Adapters (`infrastructure/out/mongo`):** MongoDB adapters, mapper functions, and
      MongoRepositories implementing the secondary ports.
    - **Configuration (`infrastructure/config`):** Spring Bean definitions and environment configs.

---

## 📂 Project Structure

```
src/main/java/com/bikemmerce/commerce/
├── application/                     <-- Application Layer
│   └── usecases/                    <-- Orchestrators of Business Actions
│       ├── commands/                <-- Input Command DTOs (e.g. CreateCustomerCommand, CreateProductCommand)
│       ├── cart/                    <-- Cart Use Cases (Add, Clear, Get, Remove)
│       ├── customer/                <-- Customer Use Cases (Create, Get, Update)
│       ├── order/                   <-- Order Use Cases (Cancel, Create, Get)
│       └── product/                 <-- Product Use Cases (Create, Delete, Get, List, Update)
│
├── domain/                          <-- Pure Domain Layer (Business Logic)
│   ├── model/                       <-- Entities and Value Objects
│   │   ├── constants/               <-- OrderStatus, etc.
│   │   ├── value/objects/           <-- Immutable Value Objects (Records: CustomerId, Email, ProductId, OrderId)
│   │   ├── Cart.java
│   │   ├── Customer.java
│   │   ├── Order.java
│   │   └── Product.java
│   ├── ports/                       <-- Outgoing Ports (Repository Interfaces)
│   │   └── *RepositoryPort.java
│   └── result/                      <-- Domain wrapper for result handling (Result<T>)
│
└── infrastructure/                  <-- Infrastructure Layer (Adapters & Frameworks)
    ├── in/                          <-- Incoming / Primary Adapters
    │   └── rest/
    │       ├── dto/                 <-- Request/Response DTOs (split by domains: cart, customer, order, product)
    │       ├── mapper/              <-- REST Request DTO -> Application Command & Domain -> REST Response Mappers
    │       └── *RestController.java <-- REST Controllers (Cart, Customer, Order, Product)
    ├── out/                         <-- Outgoing / Secondary Adapters
    │   └── mongo/
    │       ├── adapters/            <-- Outbound Port Implementations (CartMongoAdapter, CustomerMongoAdapter, etc.)
    │       ├── documents/dto/       <-- MongoDB Documents (DTOs representing database records)
    │       ├── mapper/              <-- Document DTO <-> Domain Entity Mappers
    │       └── *MongoRepository.java <-- Spring Data MongoDB Repository Interfaces
    └── config/
        └── beans/                   <-- Spring Bean Configuration for UseCases (Cart, Customer, Order, Product configs)
```

---

## 🚀 Future Roadmap

- [ ] **Apache Kafka Integration:**
    - Setup Kafka brokers.
    - Implement Event Producers to publish events when an Order is placed or cancelled.
    - Implement asynchronous consumers (e.g., notification services, updating product stock).
- [ ] **Testing Suite:**
    - Unit tests using JUnit 5 and Mockito.
    - Integration tests using Testcontainers for MongoDB and Kafka.
- [ ] **Product Images Support:**
    - Modify Product model to include images.
    - Set up a media repository to save images (local storage or remote S3 buckets).
- [ ] **Web Interface:**
    - Develop a modern web application using React or Angular.
- [ ] **Cloud Deployment:**
    - Deploy the system in AWS (utilizing Free Tier options like EC2, ECS, or Elastic Beanstalk).