package online.store.services;

import online.store.model.CheckoutRequest;
import online.store.model.Order;
import online.store.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductsService productsService;
    private final CreditCardValidationService creditCardValidationService;
    private final long maxNumberOfItems;

    public OrdersService(OrdersRepository ordersRepository,
                         ProductsService productsService,
                         CreditCardValidationService creditCardValidationService,
                         @Value("${products.service.max-number-of-items:25}") long maxNumberOfItems) {
        this.ordersRepository = ordersRepository;
        this.productsService = productsService;
        this.creditCardValidationService = creditCardValidationService;
        this.maxNumberOfItems = maxNumberOfItems;
    }

    private static boolean isNullOrBlank(String input) {
        return input == null || input.isEmpty() || input.trim().length() == 0;
    }

    public ResponseEntity<String> placeOrders(CheckoutRequest checkoutRequest) {
        if (isNullOrBlank(checkoutRequest.getCreditCard())) {
            return new ResponseEntity<>("Credit card information is missing",
                    HttpStatus.PAYMENT_REQUIRED);
        }
        if (isNullOrBlank(checkoutRequest.getFirstName())) {
            return new ResponseEntity<>("First name is missing", HttpStatus.BAD_REQUEST);
        }
        if (isNullOrBlank(checkoutRequest.getLastName())) {
            return new ResponseEntity<>("Last name is missing", HttpStatus.BAD_REQUEST);
        }

        creditCardValidationService.validate(checkoutRequest.getCreditCard());

        Set<Order> orders = getOrders(checkoutRequest);
        validateNumberOfItemsOrdered(orders);
        ordersRepository.saveAll(orders);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    private Set<Order> getOrders(CheckoutRequest checkoutRequest) {

        Set<Order> orders = new HashSet<>(checkoutRequest.getProducts().size());

        for (CheckoutRequest.ProductInfo productInfo : checkoutRequest.getProducts()) {

            Order order = new Order(checkoutRequest.getFirstName(),
                    checkoutRequest.getLastName(),
                    checkoutRequest.getEmail(),
                    checkoutRequest.getShippingAddress(),
                    productInfo.getQuantity(),
                    productsService.getProductById(productInfo.getProductId()),
                    checkoutRequest.getCreditCard());
            orders.add(order);
        }

        return orders;
    }

    private void validateNumberOfItemsOrdered(Iterable<Order> orders) {
        long totalNumberOfItems = 0;
        for (Order order: orders)  {
            totalNumberOfItems += order.getQuantity();
        }
        if (totalNumberOfItems > maxNumberOfItems) {
            throw new IllegalStateException(String.format("Number of products %d exceeded the limit of %d",
                    totalNumberOfItems, maxNumberOfItems));
        }
    }
}
