package product.dto;

import java.math.BigDecimal;

/**
 * Response DTO für Product.
 */
public record ProductResponse(
    Long id,
    String name,
    String description,
    String manufacturer,
    BigDecimal price,
    boolean available
) {}
