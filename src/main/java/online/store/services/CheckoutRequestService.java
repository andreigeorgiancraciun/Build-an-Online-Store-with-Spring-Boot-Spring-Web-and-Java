package online.store.services;

import online.store.model.CheckoutRequest;
import online.store.model.Order;
import online.store.model.Product;
import online.store.repositories.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutRequestService {
    private final OrdersRepository ordersRepository;
    private final ProductsService productsService;

    public CheckoutRequestService(OrdersRepository ordersRepository,
                                  ProductsService productsService) {
        this.ordersRepository = ordersRepository;
        this.productsService = productsService;
    }


    public void checkout(CheckoutRequest checkoutRequest) {
        String firstName = checkoutRequest.getFirstName();
        String lastName = checkoutRequest.getLastName();
        String email = checkoutRequest.getEmail();
        String shippingAddress = checkoutRequest.getShippingAddress();
        String creditCard = checkoutRequest.getCreditCard();
        List<CheckoutRequest.ProductInfo> productInfos = checkoutRequest.getProducts();

        for (CheckoutRequest.ProductInfo productInfo : productInfos) {
            Product product = productsService.getProductById(productInfo.getProductId());
            long quantity = productInfo.getQuantity();
            Order order = new Order(firstName, lastName, email, shippingAddress, quantity, product, creditCard);
            ordersRepository.save(order);
        }
    }
}
