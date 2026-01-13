# Hexagonal Architecture (Ports & Adapters)

## Konzept

Die Hexagonale Architektur trennt die Anwendung in drei Bereiche:
- **Domain**: Geschäftslogik, Value Objects, Entities
- **Application**: Use Cases, Ports (Interfaces)
- **Adapter**: Implementierungen für I/O (REST, DB, Email, etc.)

## Architektur-Diagramm

```mermaid
graph TB
    subgraph Adapters ["ADAPTER LAYER"]
        subgraph Primary ["Primary Adapters (Driving)"]
            REST[OrderController<br/>ProductController]
        end
        subgraph Secondary ["Secondary Adapters (Driven)"]
            DB[(Persistence<br/>Adapter)]
            EMAIL[Email<br/>Adapter]
            API[External API<br/>Adapter]
        end
    end

    subgraph Application ["APPLICATION LAYER"]
        subgraph InputPorts ["Input Ports"]
            GOU[GetOrderUseCase]
            UOU[UpdateOrderUseCase]
            COU[ConfirmOrderUseCase]
        end
        subgraph Service ["Application Service"]
            OS[OrderService]
        end
        subgraph OutputPorts ["Output Ports"]
            LOP[LoadOrderPort]
            SOP[SaveOrderPort]
            SNP[SendNotificationPort]
            CEP[CheckOrderExistsPort]
        end
    end

    subgraph Domain ["DOMAIN LAYER"]
        subgraph VO ["Value Objects"]
            OrderId
            Money
            Quantity
            CustomerId
        end
        subgraph Entities ["Entities"]
            Order
            OrderItem
            OrderConfirmation
        end
        subgraph DS ["Domain Services"]
            Calculator[OrderConfirmation<br/>Calculator]
        end
    end

    REST --> InputPorts
    InputPorts --> OS
    OS --> Domain
    OS --> OutputPorts
    OutputPorts -.->|implemented by| Secondary

    style Domain fill:#e8f5e9
    style Application fill:#fff3e0
    style Adapters fill:#e3f2fd
```

## Dateistruktur

```
hexagonal/
├── src/main/java/
│   ├── order/                              # ORDER BOUNDED CONTEXT
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── OrderId.java            # Value Object
│   │   │   │   ├── CustomerId.java
│   │   │   │   ├── ProductId.java          # Anti-Corruption Layer
│   │   │   │   ├── Money.java
│   │   │   │   ├── Quantity.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   ├── Order.java              # Aggregate Root
│   │   │   │   ├── OrderItem.java
│   │   │   │   └── OrderConfirmation.java
│   │   │   ├── exception/
│   │   │   │   ├── OrderNotFoundException.java
│   │   │   │   └── OrderAlreadyExistsException.java
│   │   │   └── service/
│   │   │       └── OrderConfirmationCalculator.java
│   │   ├── application/
│   │   │   ├── port/
│   │   │   │   ├── input/
│   │   │   │   │   ├── GetOrderUseCase.java
│   │   │   │   │   ├── UpdateOrderUseCase.java
│   │   │   │   │   └── ConfirmOrderUseCase.java
│   │   │   │   └── output/
│   │   │   │       ├── LoadOrderPort.java
│   │   │   │       ├── SaveOrderPort.java
│   │   │   │       ├── SaveConfirmationPort.java
│   │   │   │       ├── SendNotificationPort.java
│   │   │   │       └── CheckOrderExistsPort.java
│   │   │   ├── dto/
│   │   │   │   ├── OrderItemRequest.java
│   │   │   │   ├── UpdateOrderCommand.java
│   │   │   │   ├── OrderResponse.java
│   │   │   │   ├── OrderItemResponse.java
│   │   │   │   └── OrderConfirmationResponse.java
│   │   │   ├── mapper/
│   │   │   │   └── OrderMapper.java
│   │   │   └── service/
│   │   │       └── OrderService.java
│   │   └── adapter/
│   │       ├── input/rest/
│   │       │   └── OrderController.java
│   │       ├── output/
│   │       │   ├── persistence/
│   │       │   │   ├── OrderPersistenceAdapter.java
│   │       │   │   └── ConfirmationPersistenceAdapter.java
│   │       │   ├── notification/
│   │       │   │   └── EmailNotificationAdapter.java
│   │       │   └── external/
│   │       │       └── ExternalOrderApiAdapter.java
│   │       └── config/
│   │           └── OrderConfiguration.java
│   │
│   └── product/                            # PRODUCT BOUNDED CONTEXT
│       ├── domain/model/
│       ├── application/
│       └── adapter/
│
└── pom.xml
```

## Senior-Level Patterns

### Value Objects
```java
public record OrderId(Long value) {
    public OrderId {
        Objects.requireNonNull(value);
        if (value <= 0) throw new IllegalArgumentException();
    }
    public static OrderId of(Long value) { return new OrderId(value); }
}
```

### Ports & Adapters
```mermaid
classDiagram
    class LoadOrderPort {
        <<interface>>
        +loadById(OrderId) Optional~Order~
    }
    class OrderPersistenceAdapter {
        +loadById(OrderId) Optional~Order~
    }

    LoadOrderPort <|.. OrderPersistenceAdapter : implements
```

### Anti-Corruption Layer
- `ProductId` ist im Order-Modul definiert
- Kein Import aus dem Product-Modul
- Bounded Contexts bleiben entkoppelt

## Ablauf: Order bestätigen

```mermaid
sequenceDiagram
    participant C as Controller
    participant S as OrderService
    participant Ext as ExternalApiAdapter
    participant DB as PersistenceAdapter
    participant Email as EmailAdapter

    C->>S: confirmOrder(orderId)
    S->>DB: loadById(orderId)
    DB-->>S: Order
    S->>Ext: existsInExternalSystem(orderId)
    Ext-->>S: false
    S->>S: order.confirm()
    S->>S: calculator.calculate(order)
    S->>DB: save(order)
    S->>DB: save(confirmation)
    S->>Email: sendOrderConfirmation()
    S-->>C: OrderConfirmationResponse
```

## Vorteile

- **Testbar**: Ports können gemockt werden
- **Flexibel**: Adapter austauschbar (DB → In-Memory, SMTP → SendGrid)
- **Clean Dependencies**: Domain hat keine externen Abhängigkeiten
- **Use Case fokussiert**: Jeder Port = ein Use Case
