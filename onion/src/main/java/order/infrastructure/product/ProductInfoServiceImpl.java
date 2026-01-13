package order.infrastructure.product;

import order.application.service.ProductInfoService;
import order.core.model.ProductId;
import product.application.repository.ProductRepository;
import product.core.model.Product;

import java.util.Optional;

/**
 * Infrastructure - Implementierung des ProductInfoService.
 *
 * Brücke zum Product-Modul.
 */
public class ProductInfoServiceImpl implements ProductInfoService {

    private final ProductRepository productRepository;

    public ProductInfoServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductInfo> getProductInfo(ProductId productId) {
        // Konvertierung: Order.ProductId → Product.ProductId
        product.core.model.ProductId productProductId =
            product.core.model.ProductId.of(productId.value());

        return productRepository.findById(productProductId)
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
