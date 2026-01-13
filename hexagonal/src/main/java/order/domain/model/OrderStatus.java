package order.domain.model;

public enum OrderStatus {
    DRAFT,
    CONFIRMED,
    CANCELLED;

    public boolean isModifiable() {
        return this == DRAFT;
    }
}
