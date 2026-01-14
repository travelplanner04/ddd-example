# Domain-Driven Design (DDD) - Begriffe & Konzepte

Dieses Tutorial erklärt die wichtigsten DDD-Begriffe anhand konkreter Beispiele aus diesem Projekt.

---

## Inhaltsverzeichnis

1. [Was ist DDD?](#1-was-ist-ddd)
2. [Entity](#2-entity)
3. [Value Object](#3-value-object)
4. [Aggregate & Aggregate Root](#4-aggregate--aggregate-root)
5. [Domain Service](#5-domain-service)
6. [Repository](#6-repository)
7. [Bounded Context](#7-bounded-context)
8. [Anti-Corruption Layer](#8-anti-corruption-layer)
9. [Ubiquitous Language](#9-ubiquitous-language)
10. [Factory Pattern](#10-factory-pattern)
11. [Domain Events](#11-domain-events)
12. [Zusammenfassung](#12-zusammenfassung)

---

## 1. Was ist DDD?

> **Domain-Driven Design** ist ein Ansatz, der die Fachdomäne in den Mittelpunkt der Softwareentwicklung stellt.

```
┌─────────────────────────────────────────────────────────────────────┐
│                         DDD = Denkansatz                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌─────────────┐     ┌─────────────┐     ┌─────────────┐          │
│   │  Fachliche  │     │  Gemeinsame │     │   Code      │          │
│   │  Probleme   │────▶│   Sprache   │────▶│   spiegelt  │          │
│   │  verstehen  │     │  entwickeln │     │   Fachlogik │          │
│   └─────────────┘     └─────────────┘     └─────────────┘          │
│                                                                     │
│   "Was macht das         "Order wird         order.confirm()       │
│    Geschäft?"             bestätigt"                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### DDD ist NICHT:

| Falsch | Richtig |
|--------|---------|
| Ein `domain/` Ordner | Ein Denkansatz |
| Eine Architektur (Onion, Hexagonal) | Unabhängig von Architektur |
| Eine Package-Struktur | Wie du über Probleme nachdenkst |

---

## 2. Entity

> Eine **Entity** hat eine **Identität**, die über die Zeit bestehen bleibt.

### Merkmale

```
┌──────────────────────────────────────────────────────────────┐
│                         ENTITY                                │
├──────────────────────────────────────────────────────────────┤
│  ✓ Hat eindeutige ID                                         │
│  ✓ Gleichheit basiert auf ID, nicht auf Attributen           │
│  ✓ Kann sich über Zeit ändern, bleibt aber "dieselbe"        │
│  ✓ Hat Lebenszyklus (erstellt → verändert → gelöscht)        │
└──────────────────────────────────────────────────────────────┘
```

### Projektbeispiel: Order

```java
// hexagonal/src/main/java/order/domain/model/Order.java

public class Order {
    private final OrderId id;           // ← IDENTITÄT
    private final CustomerId customerId;
    private List<OrderItem> items;
    private OrderStatus status;         // ← Kann sich ändern

    // Gleichheit basiert NUR auf ID
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);  // ← NUR ID!
    }
}
```

### Visualisierung

```
Order #42                    Order #42                    Order #42
┌────────────┐              ┌────────────┐              ┌────────────┐
│ id: 42     │              │ id: 42     │              │ id: 42     │
│ status:    │   ──────▶    │ status:    │   ──────▶    │ status:    │
│   DRAFT    │    Item      │   DRAFT    │   confirm()  │ CONFIRMED  │
│ items: []  │    added     │ items: [1] │              │ items: [1] │
└────────────┘              └────────────┘              └────────────┘
     │                            │                            │
     └────────────────────────────┴────────────────────────────┘
                    DIESELBE Entity (ID bleibt)
```

---

## 3. Value Object

> Ein **Value Object** hat **keine Identität** - es ist definiert durch seine Werte.

### Merkmale

```
┌──────────────────────────────────────────────────────────────┐
│                      VALUE OBJECT                             │
├──────────────────────────────────────────────────────────────┤
│  ✓ Keine eigene ID                                           │
│  ✓ Gleichheit basiert auf ALLEN Attributen                   │
│  ✓ Immutable (unveränderlich)                                │
│  ✓ Austauschbar - zwei gleiche VOs sind identisch            │
│  ✓ Kann Validierung und Verhalten haben                      │
└──────────────────────────────────────────────────────────────┘
```

### Projektbeispiele

```java
// hexagonal/src/main/java/order/domain/model/Money.java

public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(amount);
        amount = amount.setScale(2, RoundingMode.HALF_UP);  // ← Validierung
    }

    public Money add(Money other) {               // ← Verhalten
        return new Money(this.amount.add(other.amount));  // ← Gibt NEUES Objekt zurück
    }

    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
```

```java
// hexagonal/src/main/java/order/domain/model/OrderId.java

public record OrderId(Long value) {

    public OrderId {
        Objects.requireNonNull(value, "OrderId cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("OrderId must be positive");
        }
    }

    public static OrderId of(Long value) {
        return new OrderId(value);
    }
}
```

### Entity vs Value Object

```
┌────────────────────────────────────────────────────────────────────┐
│                    ENTITY vs VALUE OBJECT                          │
├──────────────────────────┬─────────────────────────────────────────┤
│         ENTITY           │           VALUE OBJECT                  │
├──────────────────────────┼─────────────────────────────────────────┤
│ Order                    │ OrderId, CustomerId                     │
│ Product                  │ ProductId, ProductName                  │
│ Customer                 │ Money, Price                            │
│                          │ Quantity                                │
├──────────────────────────┼─────────────────────────────────────────┤
│ Hat ID                   │ Keine ID                                │
│ Mutable                  │ Immutable                               │
│ order1 == order2         │ money1 == money2                        │
│   wenn ID gleich         │   wenn amount gleich                    │
├──────────────────────────┼─────────────────────────────────────────┤
│ "Welche Order?"          │ "Wie viel?"                             │
│ "Welches Produkt?"       │ "Welcher Preis?"                        │
└──────────────────────────┴─────────────────────────────────────────┘
```

### Warum Value Objects?

```java
// OHNE Value Object - Primitive Obsession
public void createOrder(Long orderId, Long customerId, double price) {
    // orderId und customerId können verwechselt werden!
    // price kann negativ sein!
}

// MIT Value Objects - Typsicherheit
public void createOrder(OrderId orderId, CustomerId customerId, Money price) {
    // Compiler verhindert Verwechslung!
    // Money validiert sich selbst!
}
```

---

## 4. Aggregate & Aggregate Root

> Ein **Aggregate** ist ein Cluster von Objekten, die zusammen eine Einheit bilden.
> Der **Aggregate Root** ist der einzige Einstiegspunkt.

### Konzept

```
┌─────────────────────────────────────────────────────────────────┐
│                     ORDER AGGREGATE                              │
│                                                                  │
│    ┌──────────────────────────────────────────────────────┐     │
│    │                 Order (Aggregate Root)                │     │
│    │                                                       │     │
│    │   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │     │
│    │   │ OrderItem 1 │  │ OrderItem 2 │  │ OrderItem 3 │  │     │
│    │   └─────────────┘  └─────────────┘  └─────────────┘  │     │
│    │                                                       │     │
│    └──────────────────────────────────────────────────────┘     │
│                              │                                   │
│                              ▼                                   │
│    ┌──────────────────────────────────────────────────────┐     │
│    │              KONSISTENZGRENZE                         │     │
│    │  - Items können nur über Order hinzugefügt werden     │     │
│    │  - Invarianten werden vom Root geschützt              │     │
│    └──────────────────────────────────────────────────────┘     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                               │
                               │ Referenz nur über ID
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    PRODUCT AGGREGATE                             │
│    ┌───────────────────────────────────────────────────────┐    │
│    │                Product (Aggregate Root)                │    │
│    └───────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

### Projektbeispiel: Order als Aggregate Root

```java
// hexagonal/src/main/java/order/domain/model/Order.java

/**
 * Order Aggregate Root.
 * Alle Änderungen am Order gehen über diese Klasse.
 * Schützt Invarianten des Aggregates.
 */
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private List<OrderItem> items;        // ← Teil des Aggregates
    private OrderStatus status;

    // ========== INVARIANTEN-SCHUTZ ==========

    public void addItem(OrderItem item) {
        ensureModifiable();               // ← Invariante prüfen
        this.items.add(item);
    }

    public void confirm() {
        ensureModifiable();               // ← Invariante prüfen
        if (items.isEmpty()) {
            throw new EmptyOrderException(id);  // ← Geschäftsregel
        }
        this.status = OrderStatus.CONFIRMED;
    }

    private void ensureModifiable() {
        if (!status.isModifiable()) {
            throw new OrderAlreadyConfirmedException(id);
        }
    }

    // ========== KEIN DIREKTER ZUGRIFF AUF ITEMS ==========

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);  // ← Schutz!
    }
}
```

### Aggregate-Regeln

```
┌─────────────────────────────────────────────────────────────────┐
│                      AGGREGATE REGELN                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. NUR über Root zugreifen                                      │
│     ✗ orderItem.setQuantity(5)                                   │
│     ✓ order.updateItemQuantity(productId, 5)                     │
│                                                                  │
│  2. Referenzen zwischen Aggregates NUR über ID                   │
│     ✗ OrderItem hat Product product                              │
│     ✓ OrderItem hat ProductId productId                          │
│                                                                  │
│  3. Ein Repository pro Aggregate                                 │
│     ✓ OrderRepository                                            │
│     ✗ OrderItemRepository (Items sind Teil von Order!)           │
│                                                                  │
│  4. Transaktionsgrenze ≈ Konsistenzgrenze                        │
│     Starke Konsistenz wird innerhalb eines Aggregates garantiert │
│     (über Aggregates hinweg via Events/Prozess entkoppeln)       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5. Domain Service

> Ein **Domain Service** enthält Geschäftslogik, die nicht zu einer einzelnen Entity gehört.

### Wann Domain Service?

```
┌─────────────────────────────────────────────────────────────────┐
│              WANN DOMAIN SERVICE NUTZEN?                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✓ Logik betrifft MEHRERE Aggregates                            │
│  ✓ Logik passt nicht natürlich in eine Entity                   │
│  ✓ Operation ist stateless                                      │
│                                                                  │
│  ✗ NICHT für CRUD-Operationen (→ Repository)                    │
│  ✗ NICHT für Orchestrierung (→ Application Service)             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Projektbeispiel: OrderConfirmationCalculator

```java
// hexagonal/src/main/java/order/domain/service/OrderConfirmationCalculator.java

/**
 * Domain Service für Bestätigungs-Berechnung.
 *
 * Liegt im Domain Layer, da es reine Geschäftslogik ist,
 * die nicht sinnvoll in ein einzelnes Aggregate passt.
 */
public class OrderConfirmationCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");
    private static final Money BASE_SHIPPING = Money.of(5.99);
    private static final Money PER_ITEM_SHIPPING = Money.of(1.00);

    /**
     * Berechnet die Bestätigungsdetails für eine Order.
     * REINE GESCHÄFTSLOGIK - keine Infrastruktur und keine ID-Erzeugung.
     */
    public OrderConfirmationDetails calculateDetails(Order order) {
        Money totalAmount = order.calculateTotal();
        Money taxAmount = totalAmount.multiply(TAX_RATE);
        Money shippingCost = calculateShipping(order.getItemCount());

        return new OrderConfirmationDetails(totalAmount, taxAmount, shippingCost);
    }

    private Money calculateShipping(int itemCount) {
        return BASE_SHIPPING.add(PER_ITEM_SHIPPING.multiply(itemCount));
    }
}

record OrderConfirmationDetails(Money totalAmount, Money taxAmount, Money shippingCost) { }
```

### Domain Service vs Application Service

```
┌────────────────────────────────────────────────────────────────────┐
│           DOMAIN SERVICE vs APPLICATION SERVICE                    │
├─────────────────────────────┬──────────────────────────────────────┤
│       DOMAIN SERVICE        │       APPLICATION SERVICE            │
├─────────────────────────────┼──────────────────────────────────────┤
│ Geschäftslogik              │ Orchestrierung                       │
│ "Wie berechnet man Steuer?" │ "Was passiert bei Bestätigung?"      │
├─────────────────────────────┼──────────────────────────────────────┤
│ Keine Infrastruktur         │ Ruft Repositories, Services          │
│ Keine Transaktionen         │ Managed Transaktionen                │
│ Stateless                   │ Koordiniert Ablauf                   │
├─────────────────────────────┼──────────────────────────────────────┤
│ OrderConfirmationCalculator │ OrderService.confirmOrder()          │
│ PricingService              │ OrderService.updateOrder()           │
└─────────────────────────────┴──────────────────────────────────────┘
```

---

## 6. Repository

> Ein **Repository** abstrahiert den Datenzugriff und gibt Domain-Objekte zurück.

### Konzept

```
┌─────────────────────────────────────────────────────────────────┐
│                        REPOSITORY                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Domain Layer                 Infrastructure Layer              │
│   ┌─────────────────┐         ┌─────────────────────────┐       │
│   │   OrderService  │         │  OrderPersistenceAdapter │       │
│   │                 │         │                          │       │
│   │  loadOrderPort ─┼────────▶│  implements LoadOrderPort│       │
│   │                 │         │                          │       │
│   └─────────────────┘         │  - Map<Long, Entity>     │       │
│           │                   │  - mapToDomain()         │       │
│           │                   │  - mapToEntity()         │       │
│           ▼                   └─────────────────────────┘       │
│   ┌─────────────────┐                                           │
│   │  LoadOrderPort  │  ← Interface im Domain/Application Layer  │
│   │  (Interface)    │                                           │
│   └─────────────────┘                                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Projektbeispiel: Hexagonal (mit Interface)

```java
// hexagonal/src/main/java/order/application/port/output/LoadOrderPort.java

/**
 * Output Port: Order aus Persistenz laden.
 */
public interface LoadOrderPort {
    Optional<Order> loadById(OrderId orderId);
}
```

```java
// hexagonal/src/main/java/order/adapter/output/persistence/OrderPersistenceAdapter.java

/**
 * Secondary Adapter - Persistenz.
 * Implementiert Output Ports für Order-Persistierung.
 */
public class OrderPersistenceAdapter implements LoadOrderPort, SaveOrderPort {

    private final Map<Long, OrderEntity> database = new HashMap<>();

    @Override
    public Optional<Order> loadById(OrderId orderId) {
        OrderEntity entity = database.get(orderId.value());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDomain(entity));  // ← Entity → Domain Object
    }

    private Order mapToDomain(OrderEntity entity) {
        // Mapping von Persistence Entity zu Domain Model
        return Order.reconstitute(
            OrderId.of(entity.id),
            CustomerId.of(entity.customerId),
            mapItemsToDomain(entity.items),
            OrderStatus.valueOf(entity.status)
        );
    }
}
```

### Projektbeispiel: Layered (ohne Interface)

```java
// layered/src/main/java/order/repository/OrderRepository.java

/**
 * Data Access Layer - Order Repository.
 * In Layered Architecture wird das Repository oft als konkrete Klasse genutzt.
 */
public class OrderRepository {

    private final Map<Long, OrderData> database = new HashMap<>();

    public Optional<Order> findById(OrderId orderId) {
        // Direkte Implementierung
    }
}
```

### Repository-Vergleich

```
┌─────────────────────────────────────────────────────────────────┐
│                   REPOSITORY VERGLEICH                           │
├───────────────────┬─────────────────────────────────────────────┤
│     LAYERED       │        HEXAGONAL / ONION                    │
├───────────────────┼─────────────────────────────────────────────┤
│ OrderRepository   │ LoadOrderPort (Interface)                   │
│ (konkrete Klasse) │ OrderPersistenceAdapter (Implementierung)   │
├───────────────────┼─────────────────────────────────────────────┤
│ Service → Repo    │ Service → Port ← Adapter                    │
│ (direkt)          │ (über Interface)                            │
├───────────────────┼─────────────────────────────────────────────┤
│ Einfacher         │ Austauschbar                                │
│ Weniger Dateien   │ Testbar mit Mocks                           │
└───────────────────┴─────────────────────────────────────────────┘
```

---

## 7. Bounded Context

> Ein **Bounded Context** ist ein abgegrenzter Bereich, in dem ein bestimmtes Modell gilt.

### Konzept

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│    ┌───────────────────────────────────────────────────────┐    │
│    │                   ORDER CONTEXT                        │    │
│    │                                                        │    │
│    │   "Order" = Bestellung mit Items, Status, Total       │    │
│    │   "Product" = nur ProductId (Referenz)                │    │
│    │   "Confirm" = Bestellung abschließen                  │    │
│    │                                                        │    │
│    │   ┌────────┐  ┌───────────┐  ┌──────────────────┐     │    │
│    │   │ Order  │  │ OrderItem │  │ OrderConfirmation│     │    │
│    │   └────────┘  └───────────┘  └──────────────────┘     │    │
│    │        │                                               │    │
│    └────────┼───────────────────────────────────────────────┘    │
│             │                                                     │
│             │ ProductId (nur ID, nicht das ganze Product!)       │
│             ▼                                                     │
│    ┌───────────────────────────────────────────────────────┐    │
│    │                  PRODUCT CONTEXT                       │    │
│    │                                                        │    │
│    │   "Product" = Artikel mit Preis, Lager, Hersteller    │    │
│    │   "Stock" = Lagerbestand                              │    │
│    │   "Reserve" = Bestand reservieren                     │    │
│    │                                                        │    │
│    │   ┌─────────┐                                         │    │
│    │   │ Product │                                         │    │
│    │   └─────────┘                                         │    │
│    │                                                        │    │
│    └───────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Im Projekt

```
ddd/
├── hexagonal/
│   └── src/main/java/
│       ├── order/                    ← ORDER BOUNDED CONTEXT
│       │   ├── domain/model/
│       │   │   ├── Order.java
│       │   │   ├── OrderItem.java
│       │   │   ├── ProductId.java    ← EIGENE ProductId!
│       │   │   └── ...
│       │   ├── application/
│       │   └── adapter/
│       │
│       └── product/                  ← PRODUCT BOUNDED CONTEXT
│           ├── domain/model/
│           │   ├── Product.java
│           │   ├── ProductId.java    ← EIGENE ProductId!
│           │   └── ...
│           ├── application/
│           └── adapter/
```

### Gleicher Name, andere Bedeutung

```java
// order/domain/model/ProductId.java
package order.domain.model;

/**
 * WICHTIG: Liegt im Order-Modul, NICHT importiert aus Product-Modul!
 * Das ist ein Baustein der Entkopplung: Order kennt nur eine eigene ProductId.
 * Ein kompletter ACL ist typischerweise ein Adapter/Translator an der Context-Grenze.
 */
public record ProductId(Long value) { ... }


// product/domain/model/ProductId.java
package product.domain.model;

/**
 * ProductId im Product Context.
 */
public record ProductId(Long value) { ... }
```

---

## 8. Anti-Corruption Layer

> Der **Anti-Corruption Layer (ACL)** schützt einen Bounded Context vor fremden Modellen.

### Das Problem

```
OHNE Anti-Corruption Layer:

┌──────────────────┐         ┌──────────────────┐
│   ORDER CONTEXT  │         │  PRODUCT CONTEXT │
│                  │         │                  │
│   OrderItem      │────────▶│   Product        │
│   - product: ────┼─────────┼──▶ (ganzes       │
│     Product      │         │     Objekt!)     │
│                  │         │                  │
└──────────────────┘         └──────────────────┘

Problem: Order ist jetzt an Product's Struktur gekoppelt!
         Ändert sich Product → Order muss angepasst werden.
```

### Die Lösung

```
MIT Anti-Corruption Layer:

┌──────────────────┐         ┌──────────────────┐
│   ORDER CONTEXT  │         │  PRODUCT CONTEXT │
│                  │         │                  │
│   OrderItem      │         │   Product        │
│   - productId: ──┼────X────┼──▶              │
│     ProductId    │   │     │                  │
│     (EIGENE!)    │   │     │                  │
│                  │   │     │                  │
│   ┌────────────┐ │   │     │                  │
│   │ Translator │◀┼───┘     │                  │
│   │ / Adapter  │ │         │                  │
│   └────────────┘ │         │                  │
└──────────────────┘         └──────────────────┘

Nur primitive Werte (oder spezifische DTOs) über die Grenze,
nicht das fremde Domänenmodell.
```

### Projektbeispiel

```java
// hexagonal/src/main/java/order/domain/model/OrderItem.java

/**
 * WICHTIG: Referenziert Product nur über ProductId (Value Object).
 * Kein Import aus Product-Modul = keine Kopplung zwischen Bounded Contexts.
 */
public class OrderItem {

    private final ProductId productId;  // ← order.domain.model.ProductId!
    private final Quantity quantity;
    private final Money unitPrice;

    // ...
}
```

```java
// hexagonal/src/main/java/order/application/service/OrderService.java

private void reserveStockForOrder(Order order) {
    for (OrderItem item : order.getItems()) {
        // KONVERTIERUNG: Order.ProductId → Product.ProductId
        product.domain.model.ProductId productId =
            product.domain.model.ProductId.of(item.getProductId().value());
        //                                   ▲
        //                          Nur der Long-Wert wird übergeben!

        reserveStockUseCase.reserveStock(productId, item.getQuantity());
    }
}
```

### Einfaches ACL-Beispiel (Translator)

Idee: Der Order-Context will nur wissen, ob ein Produkt bestellbar ist und welchen Preis es hat.
Statt das Product-Domain-Objekt zu importieren, definiert Order ein eigenes DTO und ein Port.
Ein Adapter übersetzt dann zur API des Product-Contexts.

```
┌───────────────────────────────────────────────────────────────────┐
│                           ORDER CONTEXT                            │
│                                                                   │
│  Use Case / Service ───────▶ ProductCatalogPort                    │
│                               ▲                                   │
│                               │                                   │
│                         (Interface/Port)                           │
│                               │                                   │
└───────────────────────────────┼───────────────────────────────────┘
                                │ implements
                                ▼
┌───────────────────────────────────────────────────────────────────┐
│                        ACL (Adapter / Translator)                  │
│                                                                   │
│  ProductCatalogAclAdapter                                          │
│   - mappt Order.ProductId → Product.ProductId                      │
│   - mappt Product-Modell/API → Order.ProductInfo                   │
│                                                                   │
└───────────────────────────────┼───────────────────────────────────┘
                                │ calls
                                ▼
┌───────────────────────────────────────────────────────────────────┐
│                          PRODUCT CONTEXT                           │
│                                                                   │
│  ProductQueryApi / Repository / Service                            │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

```java
// order/application/port/output/ProductCatalogPort.java

public interface ProductCatalogPort {
    ProductInfo loadProductInfo(order.domain.model.ProductId productId);
}

record ProductInfo(order.domain.model.ProductId productId, Money unitPrice, boolean orderable) { }
```

```java
// order/adapter/output/acl/ProductCatalogAclAdapter.java

public class ProductCatalogAclAdapter implements ProductCatalogPort {

    private final product.application.port.input.GetProductQuery getProductQuery;

    public ProductCatalogAclAdapter(product.application.port.input.GetProductQuery getProductQuery) {
        this.getProductQuery = getProductQuery;
    }

    @Override
    public ProductInfo loadProductInfo(order.domain.model.ProductId orderProductId) {
        product.domain.model.ProductId productId = product.domain.model.ProductId.of(orderProductId.value());

        product.application.dto.ProductDto dto = getProductQuery.getById(productId);

        Money price = new Money(dto.price());
        boolean orderable = dto.stockQuantity() > 0 && dto.active();

        return new ProductInfo(orderProductId, price, orderable);
    }
}
```

Wichtig: Der Order-Context hängt hier nur an seinem eigenen Port und seinem eigenen DTO.
Die Übersetzung zur Product-Welt ist im Adapter gekapselt.

### ACL-Strategien

```
┌─────────────────────────────────────────────────────────────────┐
│                    ACL STRATEGIEN                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. EIGENE ID-TYPEN (wie im Projekt)                            │
│     order.domain.model.ProductId ≠ product.domain.model.ProductId│
│     (verhindert direkte Modell-Kopplung)                         │
│                                                                  │
│  2. ADAPTER/TRANSLATOR (typischer ACL)                           │
│     ProductCatalogPort.loadProductInfo(orderProductId)            │
│       → gibt ProductInfo zurück (DTO für Order-Context)           │
│                                                                  │
│  3. SHARED KERNEL (bei enger Zusammenarbeit)                     │
│     Gemeinsame Value Objects in eigenem Modul                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 9. Ubiquitous Language

> Die **Ubiquitous Language** ist die gemeinsame Sprache zwischen Entwicklern und Fachexperten.

### Konzept

```
┌─────────────────────────────────────────────────────────────────┐
│                   UBIQUITOUS LANGUAGE                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Fachexperte sagt:          Code zeigt:                        │
│   ─────────────────          ──────────────                     │
│   "Order bestätigen"         order.confirm()                    │
│   "Bestellung ist leer"      EmptyOrderException                │
│   "Artikel hinzufügen"       order.addItem(item)                │
│   "Gesamtbetrag berechnen"   order.calculateTotal()             │
│   "Lagerbestand reservieren" product.reserveStock(qty)          │
│                                                                  │
│   NICHT:                                                        │
│   ─────────────────          ──────────────                     │
│   "Order bestätigen"         orderService.process()      ← NEIN │
│   "Bestellung ist leer"      ValidationException         ← NEIN │
│   "Artikel hinzufügen"       orderDao.insertItem()       ← NEIN │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Im Projekt

```java
// Fachsprache im Code:

// "Eine Order kann nur im DRAFT-Status modifiziert werden"
private void ensureModifiable() {
    if (!status.isModifiable()) {
        throw new OrderAlreadyConfirmedException(id);
    }
}

// "Eine leere Order kann nicht bestätigt werden"
public void confirm() {
    ensureModifiable();
    if (items.isEmpty()) {
        throw new EmptyOrderException(id);
    }
    this.status = OrderStatus.CONFIRMED;
}

// "Lagerbestand für Produkt reservieren"
public void reserveStock(int quantity) {
    if (!hasEnoughStock(quantity)) {
        throw new InsufficientStockException(this.id, quantity, this.stockQuantity);
    }
    this.stockQuantity -= quantity;
}
```

---

## 10. Factory Pattern

> **Factories** kapseln die komplexe Erstellung von Objekten.

### Im Projekt

```java
// hexagonal/src/main/java/order/domain/model/Order.java

public class Order {

    // Privater Konstruktor - erzwingt Factory-Nutzung
    private Order(OrderId id, CustomerId customerId,
                  List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    // Factory für NEUE Orders
    public static Order create(OrderId id, CustomerId customerId) {
        return new Order(id, customerId, new ArrayList<>(), OrderStatus.DRAFT);
    }
    //                                                        ▲
    //                                    Neue Order ist immer DRAFT!

    // Factory für REKONSTRUKTION aus Persistenz
    public static Order reconstitute(OrderId id, CustomerId customerId,
                                      List<OrderItem> items, OrderStatus status) {
        return new Order(id, customerId, items, status);
    }
    //                                           ▲
    //                          Persistenz darf jeden Status setzen
}
```

### Warum zwei Factories?

```
┌─────────────────────────────────────────────────────────────────┐
│                    ZWEI FACTORY-ARTEN                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   create()              │   reconstitute()                       │
│   ──────────            │   ──────────────                       │
│   Für NEUE Objekte      │   Für LADEN aus DB                     │
│   Setzt Defaults        │   Übernimmt alle Werte                 │
│   Validiert Invarianten │   Vertraut der DB                      │
│                         │                                        │
│   Order.create(id, cust)│   Order.reconstitute(id, cust,         │
│   → Status = DRAFT      │                       items, status)   │
│   → Items = leer        │   → Alles wie gespeichert              │
│                         │                                        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 11. Domain Events

> **Domain Events** signalisieren, dass etwas Wichtiges in der Domäne passiert ist.

### Konzept

```
┌─────────────────────────────────────────────────────────────────┐
│                     DOMAIN EVENTS                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌─────────────┐       Event        ┌─────────────────────┐    │
│   │   Order     │ ─────────────────▶ │ OrderConfirmedEvent │    │
│   │  confirm()  │                    │ - orderId           │    │
│   └─────────────┘                    │ - confirmedAt       │    │
│                                      └──────────┬──────────┘    │
│                                                 │               │
│                         ┌───────────────────────┼───────────┐   │
│                         ▼                       ▼           ▼   │
│                 ┌──────────────┐     ┌──────────────┐  ┌──────┐ │
│                 │   Email      │     │  Inventory   │  │ Audit│ │
│                 │   Service    │     │   Service    │  │  Log │ │
│                 └──────────────┘     └──────────────┘  └──────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Im Projekt (vereinfachtes Beispiel)

```java
// hexagonal/src/main/java/product/domain/model/Product.java

public void reserveStock(int quantity) {
    if (!hasEnoughStock(quantity)) {
        throw new InsufficientStockException(this.id, quantity, this.stockQuantity);
    }
    this.stockQuantity -= quantity;

    StockReservedEvent event = new StockReservedEvent(this.id, quantity);
    domainEvents.add(event);
}

record StockReservedEvent(ProductId productId, int quantity) { }
```

### Hinweis zur Publikation

In DDD erzeugt die Domain typischerweise Events, aber veröffentlicht sie nicht selbst über Infrastruktur.
Häufig sammelt das Aggregate die Events, und der Application Layer publiziert sie nach erfolgreichem Commit.

---

## 12. Zusammenfassung

### Alle Konzepte auf einen Blick

```
┌─────────────────────────────────────────────────────────────────┐
│                    DDD KONZEPTE ÜBERSICHT                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   BUILDING BLOCKS (Taktisch)                                    │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  Entity         │ Objekt mit Identität (Order)          │   │
│   │  Value Object   │ Objekt ohne Identität (Money, OrderId)│   │
│   │  Aggregate      │ Konsistenz-Cluster (Order + Items)    │   │
│   │  Aggregate Root │ Einstiegspunkt (Order)                │   │
│   │  Domain Service │ Stateless Logik (Calculator)          │   │
│   │  Repository     │ Persistenz-Abstraktion                │   │
│   │  Factory        │ Objekt-Erstellung (create/reconstitute)│   │
│   │  Domain Event   │ Fachliches Ereignis                   │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   STRATEGIC DESIGN                                               │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  Bounded Context     │ Abgegrenzter Bereich             │   │
│   │  Ubiquitous Language │ Gemeinsame Fachsprache           │   │
│   │  Anti-Corruption Layer│ Schutz vor fremden Modellen     │   │
│   │  Context Map         │ Beziehungen zwischen Contexts    │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Projekt-Mapping

| DDD Konzept | Im Projekt |
|-------------|------------|
| **Entity** | `Order`, `Product` |
| **Value Object** | `Money`, `OrderId`, `ProductId`, `Quantity` |
| **Aggregate Root** | `Order` (enthält `OrderItem`), `Product` |
| **Domain Service** | `OrderConfirmationCalculator` |
| **Repository** | `LoadOrderPort`/`OrderPersistenceAdapter` (Hex), `OrderRepository` (Layered) |
| **Factory** | `Order.create()`, `Order.reconstitute()` |
| **Bounded Context** | `order/`, `product/` |
| **Anti-Corruption Layer** | Separate `ProductId` in jedem Context |

---

## Weiterführend

- [tutorialOverview.md](tutorialOverview.md) - Wie DDD mit Architektur zusammenhängt
- [tutorialRichVsAnemic.md](tutorialRichVsAnemic.md) - Rich vs Anemic Domain Model
- [tutorialOnion.md](tutorialOnion.md) - Onion Architecture
- [tutorialHexagonal.md](tutorialHexagonal.md) - Hexagonal Architecture
