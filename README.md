# Bikemmerce

Bikemmerce is a personal e-commerce project designed to demonstrate clean coding practices, modern Java development, and scalable system design. The project is focused on selling bicycles and related sports equipment.

This project is built using **Hexagonal Architecture (Ports & Adapters)** to keep the core business logic completely separated from external frameworks, databases, and message queues.

> [!NOTE]
> This is a work in progress and will continuously evolve over time. More features and integrations (like Kafka events) will be added incrementally.

---

## 🛠️ Technology Stack
- **Backend:** Spring Boot (Java 21)
- **Database:** MongoDB (for e-commerce catalog, orders, and cart storage)
- **Event Streaming:** Apache Kafka (to be integrated for order events, notifications, etc.)
- **Build Tool:** Gradle
- **Boilerplate Reduction:** Lombok

---

## 🏛️ Architectural Overview

This project follows **Hexagonal Architecture (Ports and Adapters)**, dividing the codebase into three main layers:

1. **Domain:** The core business rules. Free of any framework dependencies (Spring, MongoDB, etc.). Contains models, value objects, domain results, and repository ports (interfaces).
2. **Application (Use Cases):** Orchestrates the flow of data to and from the domain. Contains the execution logic for business actions (e.g., adding items to cart, completing orders, creating products).
3. **Adapters (Infrastructure):** Implements the ports defined by the domain. Divided into:
   - **Incoming (Primary) Adapters:** REST APIs, controllers, CLI inputs.
   - **Outgoing (Secondary) Adapters:** MongoDB repositories, Kafka event producers, external API clients.

### Conceptual Diagram (Hexagonal Architecture)

```
        +-------------------------------------------------------------+
        |                          ADAPTERS                           |
        |                                                             |
        |   +-----------------------------------------------------+   |
        |   |                     APPLICATION                     |   |
        |   |                                                     |   |
        |   |   +---------------------------------------------+   |   |
        |   |   |                   DOMAIN                    |   |   |
        |   |   |                                             |   |   |
        |   |   |  [Product]       [Cart]        [Order]      |   |   |
        |   |   |  [ProductId]     [ShoppingItem]             |   |   |
        |   |   +---------------------------------------------+   |   |
        |   |          ^                               ^          |   |
        |   |          | (implements)                  | (calls)  |   |
        |   |          |                               |          |   |
        |   |      [Use Cases]                    [Ports]         |   |
        |   +------/--------\--------------------/--------\-------+   |
        |         /          \                  /          \          |
        |   [ProductController]            [ProductMongoAdapter]      |
        |      (REST API)                     (MongoDB / Persistence)  |
        +-------|----------------------------------|------------------+
                v                                  v
           HTTP Client                         MongoDB DB
```

---

## 📂 Project Structure

```
src/main/java/com/bikemmerce/commerce/
├── adapters/                        <-- Adapters (Infrastructure Layer)
│   ├── in/                          <-- Incoming / Primary Adapters
│   │   └── rest/
│   │       ├── dto/                 <-- Request/Response DTOs
│   │       ├── mapper/              <-- DTO <-> Domain Mappers
│   │       └── ProductController.java
│   └── out/                         <-- Outgoing / Secondary Adapters
│       └── mongo/
│           ├── adapters/            <-- Port Implementations
│           │   └── ProductMongoAdapter.java
│           ├── document/            <-- Database Entities
│           │   └── ProductDocument.java
│           ├── mapper/              <-- DB Entity <-> Domain Mappers
│           └── MongoProductRepository.java
│
├── application/                     <-- Application Layer
│   └── usecases/                    <-- Orchestrators of Business Actions
│       ├── cart/
│       ├── customer/
│       ├── order/
│       └── product/
│           ├── CreateProductUseCase.java
│           └── GetProductUseCase.java
│
├── domain/                          <-- Pure Domain Layer (Business Logic)
│   ├── model/                       <-- Entities and Value Objects
│   │   ├── value/objects/           <-- Immutable Value Objects (Records)
│   │   ├── Cart.java
│   │   ├── Product.java
│   │   └── ...
│   ├── ports/                       <-- Outgoing Ports (Interfaces)
│   │   └── ProductRepositoryPort.java
│   └── result/                      <-- Domain wrapper for result handling
│
└── infrastructure/                  <-- Framework Configurations
    └── config/
        └── UseCaseConfig.java       <-- Spring Bean Configuration for UseCases
```

---

## 🚀 Future Roadmap
- [ ] Implement REST Adapters and MongoDB persistence for **Orders**, **Cart**, and **Customer**.
- [ ] Set up **Apache Kafka** event bus.
- [ ] Add event-driven actions (e.g. publishing an event when an Order is confirmed, updating inventory stock asynchronously).
- [ ] Add comprehensive Integration and Unit Testing (JUnit 5, Testcontainers).