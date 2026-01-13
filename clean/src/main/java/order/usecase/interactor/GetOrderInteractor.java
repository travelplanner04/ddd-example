package order.usecase.interactor;

import order.entity.exception.OrderNotFoundException;
import order.entity.model.Order;
import order.entity.model.OrderId;
import order.usecase.boundary.input.GetOrderInputBoundary;
import order.usecase.boundary.output.OrderGateway;
import order.usecase.boundary.output.ProductInfoGateway;
import order.usecase.dto.OrderMapper;
import order.usecase.dto.OrderResponse;

/**
 * Interactor (Use Case Implementation) for getting an Order.
 * In Clean Architecture, Interactors contain the application-specific business rules.
 */
public class GetOrderInteractor implements GetOrderInputBoundary {

    private final OrderGateway orderGateway;
    private final ProductInfoGateway productInfoGateway;
    private final OrderMapper mapper;

    public GetOrderInteractor(OrderGateway orderGateway, ProductInfoGateway productInfoGateway, OrderMapper mapper) {
        this.orderGateway = orderGateway;
        this.productInfoGateway = productInfoGateway;
        this.mapper = mapper;
    }

    @Override
    public OrderResponse execute(Long orderId) {
        OrderId id = new OrderId(orderId);

        Order order = orderGateway.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Load and print product info for each item (demonstrating ACL usage)
        order.getItems().forEach(item -> {
            ProductInfoGateway.ProductInfo productInfo = productInfoGateway.loadProductInfo(item.getProductId());
            System.out.println("Product: " + productInfo.name() + " from " + productInfo.manufacturer());
        });

        return mapper.toResponse(order);
    }
}
