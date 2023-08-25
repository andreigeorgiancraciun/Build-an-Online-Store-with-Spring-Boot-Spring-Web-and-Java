package online.store.controllers;

import online.store.model.wrappers.ProductsWrapper;
import online.store.services.ProductsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomepageController {

    private final ProductsService productsService;

    public HomepageController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/categories")
    public String getProductCategories() {
        return String.join(",", productsService.getAllSupportedCategories());
    }

    @GetMapping("/deals_of_the_day/{number_of_products}")
    public ProductsWrapper getDealsOfTheDay(@PathVariable(name = "number_of_products") int numberOfProducts) {
        return new ProductsWrapper(productsService.getDealsOfTheDay(numberOfProducts));
    }

    @GetMapping("/products")
    public ProductsWrapper getProductsForCategory(@RequestParam(name = "category", required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return new ProductsWrapper(productsService.getProductsByCategory(category));
        }

        return new ProductsWrapper(productsService.getAllProducts());
    }
}
