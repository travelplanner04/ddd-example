# Onion Architecture

## Konzept

Die Onion Architecture organisiert Code in konzentrischen Schichten:
- **Core (innen)**: Domain Entities, Value Objects
- **Domain Services**: Geschäftslogik
- **Application**: Use Cases, Repository-Interfaces
- **Infrastructure (außen)**: Implementierungen, Web, DB

**Regel**: Abhängigkeiten zeigen immer nach INNEN zum Kern.

## Architektur-Diagramm

```mermaid
graph TB
    subgraph Infrastructure ["INFRASTRUCTURE (außen)"]
        Web[OrderController<br/>ProductController]
        Persist[InMemoryOrderRepository<br/>InMemoryProductRepository]
        Email[EmailNotificationService]
        ExtAPI[ExternalOrderApiService]
    end

    subgraph Application ["APPLICATION LAYER"]
        AppService[OrderApplicationService<br/>ProductApplicationService]
        RepoInterface["OrderRepository<br/>ProductRepository<br/>(Interfaces)"]
        ServiceInterface["NotificationService<br/>ExternalOrderService<br/>(Interfaces)"]
        DTO[DTOs + Mapper]
    end

    subgraph DomainServices ["DOMAIN SERVICES"]
        Calculator[OrderConfirmationCalculator]
    end

    subgraph Core ["CORE (innen)"]
        VO["Value Objects<br/>OrderId, Money, Quantity"]
        Entity["Entities<br/>Order, OrderItem, Product"]
        Exception[Domain Exceptions]
    end

    Web --> AppService
    AppService --> RepoInterface
    AppService --> ServiceInterface
    AppService --> DomainServices
    DomainServices --> Core
    Persist -.->|implements| RepoInterface
    Email -.->|implements| ServiceInterface
    ExtAPI -.->|implements| ServiceInterface

    style Core fill:#c8e6c9,stroke:#388e3c,stroke-width:3px
    style DomainServices fill:#dcedc8
    style Application fill:#fff9c4
    style Infrastructure fill:#e3f2fd
```

## Dateistruktur

```
onion/
├── src/main/java/
│   ├── order/                              # ORDER BOUNDED CONTEXT
│   │   ├── core/                           # INNERSTER KERN
│   │   │   ├── model/
│   │   │   │   ├── OrderId.java
│   │   │   │   ├── CustomerId.java
│   │   │   │   ├── ProductId.java          # Anti-Corruption Layer
│   │   │   │   ├── Money.java
│   │   │   │   ├── Quantity.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   └── OrderConfirmation.java
│   │   │   └── exception/
│   │   │       ├── OrderNotFoundException.java
│   │   │       └── OrderAlreadyExistsException.java
│   │   │
│   │   ├── domainservices/                 # DOMAIN SERVICES
│   │   │   └── OrderConfirmationCalculator.java
│   │   │
│   │   ├── application/                    # APPLICATION LAYER
│   │   │   ├── repository/
│   │   │   │   ├── OrderRepository.java          # Interface!
│   │   │   │   └── OrderConfirmationRepository.java
│   │   │   ├── service/
│   │   │   │   ├── NotificationService.java      # Interface!
│   │   │   │   └── ExternalOrderService.java     # Interface!
│   │   │   ├── dto/
│   │   │   │   ├── OrderItemRequest.java
│   │   │   │   ├── UpdateOrderCommand.java
│   │   │   │   ├── OrderResponse.java
│   │   │   │   ├── OrderItemResponse.java
│   │   │   │   └── OrderConfirmationResponse.java
│   │   │   ├── mapper/
│   │   │   │   └── OrderMapper.java
│   │   │   └── OrderApplicationService.java
│   │   │
│   │   └── infrastructure/                 # ÄUSSERSTE SCHICHT
│   │       ├── persistence/
│   │       │   ├── InMemoryOrderRepository.java
│   │       │   └── InMemoryOrderConfirmationRepository.java
│   │       ├── notification/
│   │       │   └── EmailNotificationService.java
│   │       ├── external/
│   │       │   └── ExternalOrderApiService.java
│   │       ├── web/
│   │       │   └── OrderController.java
│   │       └── config/
│   │           └── OrderModuleConfiguration.java
│   │
│   └── product/                            # PRODUCT BOUNDED CONTEXT
│       ├── core/model/
│       ├── application/
│       └── infrastructure/
│
└── pom.xml
```

## Schichten-Regel

```mermaid
graph LR
    I[Infrastructure] --> A[Application] --> DS[Domain Services] --> C[Core]

    I2[Repository Impl] -.->|implements| A2[Repository Interface]

    style C fill:#c8e6c9,stroke:#388e3c,stroke-width:3px
    style DS fill:#dcedc8
    style A fill:#fff9c4
    style I fill:#e3f2fd
```

**Wichtig**: Interfaces sind in inneren Schichten definiert, Implementierungen in äußeren.

## Unterschied zu Hexagonal

| Aspekt | Onion | Hexagonal |
|--------|-------|-----------|
| Struktur | Konzentrische Schichten | Ports & Adapters |
| Interfaces | In Application Layer | Explizite Ports |
| Fokus | Abhängigkeitsrichtung | Use Cases |
| Controller | Teil von Infrastructure | Primary Adapter |

## Senior-Level Patterns

### Repository Interface (Application Layer)
```java
public interface OrderRepository {
    Optional<Order> findById(OrderId orderId);
    Order save(Order order);
}
```

### Repository Implementation (Infrastructure)
```java
public class InMemoryOrderRepository implements OrderRepository {
    // Implementierung
}
```

## Ablauf: Order bestätigen

```mermaid
sequenceDiagram
    participant Web as OrderController
    participant App as OrderApplicationService
    participant Ext as ExternalOrderApiService
    participant Calc as Calculator
    participant Repo as InMemoryOrderRepository
    participant Email as EmailNotificationService

    Web->>App: confirmOrder(orderId)
    App->>Repo: findById(orderId)
    Repo-->>App: Order
    App->>Ext: existsInExternalSystem(orderId)
    Ext-->>App: false
    App->>App: order.confirm()
    App->>Calc: calculate(order)
    Calc-->>App: OrderConfirmation
    App->>Repo: save(order)
    App->>Repo: save(confirmation)
    App->>Email: sendOrderConfirmation()
    App-->>Web: OrderConfirmationResponse
```

## Vorteile

- **Testbar**: Interfaces erlauben einfaches Mocking
- **Domain bleibt rein**: Keine Framework-Abhängigkeiten im Kern
- **Klare Abhängigkeitsrichtung**: Immer nach innen
- **Austauschbar**: Infrastructure-Komponenten leicht ersetzbar
