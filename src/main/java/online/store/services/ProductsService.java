package online.store.services;

import online.store.model.Product;
import online.store.model.ProductCategory;
import online.store.repositories.ProductCategoryRepository;
import online.store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductsService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<String> getAllSupportedCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(ProductCategory::getCategory)
                .collect(Collectors.toList());
    }

    public List<Product> getDealsOfTheDay(int numberOfProducts) {
        return productRepository.findAtMostNumberOfProducts(numberOfProducts);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Product with id %s doesn't exist", id)));
    }
}
