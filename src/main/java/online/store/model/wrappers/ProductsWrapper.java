package online.store.model.wrappers;

import online.store.model.Product;

import java.util.Collections;
import java.util.List;

public class ProductsWrapper {
    private final List<Product> products;

    public ProductsWrapper(List<Product> products) {
        this.products = Collections.unmodifiableList(products);
    }

    public List<Product> getProducts() {
        return products;
    }
}
