package order.adapter.output.product;

import order.application.port.output.LoadProductInfoPort;
import order.domain.model.ProductId;
import product.application.port.output.LoadProductPort;
import product.domain.model.Product;

import java.util.Optional;

/**
 * Secondary Adapter - Brücke zum Product-Modul.
 *
 * Implementiert den Order-Port und nutzt den Product-Port.
 * Übersetzt zwischen den Bounded Contexts.
 */
public class ProductInfoAdapter implements LoadProductInfoPort {

    private final LoadProductPort loadProductPort;

    public ProductInfoAdapter(LoadProductPort loadProductPort) {
        this.loadProductPort = loadProductPort;
    }

    @Override
    public Optional<ProductInfo> loadProductInfo(ProductId productId) {
        // Konvertierung: Order.ProductId → Product.ProductId
        product.domain.model.ProductId productProductId =
            product.domain.model.ProductId.of(productId.value());

        return loadProductPort.loadById(productProductId)
            .map(this::toProductInfo);
    }

    private ProductInfo toProductInfo(Product product) {
        return new ProductInfo(
            product.getId().value(),
            product.getName().value(),
            product.getManufacturer()
        );
    }
}
