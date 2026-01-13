# Layered Architecture

## Konzept

Die Layered (Schichten) Architektur ist der klassische Ansatz:
- **Presentation Layer**: Controller, API-Endpunkte
- **Service Layer**: Geschäftslogik
- **Repository Layer**: Datenzugriff
- **Model Layer**: Entities, Value Objects, DTOs

**Regel**: Abhängigkeiten fließen von oben nach unten.

## Architektur-Diagramm

```mermaid
graph TB
    subgraph Presentation ["PRESENTATION LAYER"]
        OC[OrderController]
        PC[ProductController]
    end

    subgraph Service ["SERVICE LAYER"]
        OS[OrderService]
        PS[ProductService]
        ES[EmailService]
        EAS[ExternalOrderApiService]
        Calc[OrderConfirmationCalculator]
        Map[OrderMapper]
    end

    subgraph Repository ["REPOSITORY LAYER"]
        OR[OrderRepository]
        CR[OrderConfirmationRepository]
        PR[ProductRepository]
    end

    subgraph Model ["MODEL LAYER"]
        VO["Value Objects<br/>OrderId, Money, etc."]
        Entity["Entities<br/>Order, Product"]
        DTO["DTOs<br/>Request/Response"]
        Exc[Exceptions]
    end

    OC --> OS
    PC --> PS
    OS --> OR
    OS --> CR
    OS --> ES
    OS --> EAS
    OS --> Calc
    PS --> PR
    OR --> Model
    PR --> Model

    style Presentation fill:#e3f2fd
    style Service fill:#fff3e0
    style Repository fill:#f3e5f5
    style Model fill:#e8f5e9
```

## Dateistruktur

```
layered/
├── src/main/java/
│   ├── order/                              # ORDER BOUNDED CONTEXT
│   │   ├── model/                          # MODEL LAYER
│   │   │   ├── OrderId.java
│   │   │   ├── CustomerId.java
│   │   │   ├── ProductId.java
│   │   │   ├── Money.java
│   │   │   ├── Quantity.java
│   │   │   ├── OrderStatus.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java
│   │   │   └── OrderConfirmation.java
│   │   ├── exception/
│   │   │   ├── OrderNotFoundException.java
│   │   │   └── OrderAlreadyExistsException.java
│   │   ├── dto/                            # DTOs
│   │   │   ├── OrderItemRequest.java
│   │   │   ├── UpdateOrderRequest.java
│   │   │   ├── OrderResponse.java
│   │   │   ├── OrderItemResponse.java
│   │   │   └── OrderConfirmationResponse.java
│   │   ├── repository/                     # REPOSITORY LAYER
│   │   │   ├── OrderRepository.java        # Konkrete Klasse!
│   │   │   └── OrderConfirmationRepository.java
│   │   ├── service/                        # SERVICE LAYER
│   │   │   ├── OrderService.java
│   │   │   ├── EmailService.java
│   │   │   ├── ExternalOrderApiService.java
│   │   │   ├── OrderConfirmationCalculator.java
│   │   │   └── OrderMapper.java
│   │   ├── presentation/                   # PRESENTATION LAYER
│   │   │   └── OrderController.java
│   │   └── config/
│   │       └── OrderConfiguration.java
│   │
│   └── product/                            # PRODUCT BOUNDED CONTEXT
│       ├── model/
│       ├── dto/
│       ├── repository/
│       ├── service/
│       ├── presentation/
│       └── config/
│
└── pom.xml
```

## Unterschied zu Onion/Hexagonal

```mermaid
graph LR
    subgraph Layered ["LAYERED"]
        S[Service] -->|"direkte<br/>Abhängigkeit"| R[Repository]
    end

    subgraph Others ["ONION/HEXAGONAL"]
        S2[Service] --> I[Interface]
        R2[Repository] -.->|implements| I
    end

    style Layered fill:#ffcdd2
    style Others fill:#c8e6c9
```

| Aspekt | Layered | Onion/Hexagonal |
|--------|---------|-----------------|
| Repository | Konkrete Klasse | Interface + Impl |
| Abhängigkeiten | Direkt | Via Interfaces |
| Testbarkeit | Schwieriger | Einfacher (Mocking) |
| Komplexität | Einfacher | Aufwändiger |
| Flexibilität | Geringer | Höher |

## Senior-Level Patterns

### Value Objects (auch hier sinnvoll!)
```java
public record Money(BigDecimal amount) {
    public Money {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
    }
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }
}
```

### DTOs für API-Grenzen
```java
public record OrderResponse(
    Long id,
    String customerId,
    String status,
    List<OrderItemResponse> items,
    BigDecimal totalAmount
) {}
```

### Mapper für Domain ↔ DTO
```java
public class OrderMapper {
    public OrderResponse toResponse(Order order) { ... }
    public List<OrderItem> toDomainItems(List<OrderItemRequest> requests) { ... }
}
```

## Ablauf: Order bestätigen

```mermaid
sequenceDiagram
    participant C as OrderController
    participant S as OrderService
    participant Ext as ExternalOrderApiService
    participant Calc as Calculator
    participant OR as OrderRepository
    participant CR as ConfirmationRepository
    participant Email as EmailService

    C->>S: confirmOrder(orderId)
    S->>OR: findById(orderId)
    OR-->>S: Order
    S->>Ext: existsInExternalSystem(orderId)
    Ext-->>S: false
    S->>S: order.confirm()
    S->>Calc: calculate(order)
    Calc-->>S: OrderConfirmation
    S->>OR: save(order)
    S->>CR: save(confirmation)
    S->>Email: sendOrderConfirmation()
    S-->>C: OrderConfirmationResponse
```

## Vorteile

- **Einfach**: Klassische, bekannte Struktur
- **Wenig Boilerplate**: Keine Interface-Schichten nötig
- **Schnell umzusetzen**: Geringerer initialer Aufwand
- **Direkter Code-Fluss**: Leicht nachvollziehbar

## Nachteile

- **Gekoppelt**: Services direkt an Implementierungen gebunden
- **Schwerer testbar**: Mocking ohne Interfaces komplizierter
- **Weniger flexibel**: Austausch von Komponenten aufwändiger

## Wann Layered verwenden?

- Kleinere Projekte
- Prototypen / MVPs
- Teams mit wenig DDD-Erfahrung
- Wenn Einfachheit wichtiger als Flexibilität ist
