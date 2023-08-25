package online.store.controllers;

import online.store.model.CheckoutRequest;
import online.store.services.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    private final OrdersService ordersService;

    public CheckoutController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        return ordersService.placeOrders(checkoutRequest);
    }
}
