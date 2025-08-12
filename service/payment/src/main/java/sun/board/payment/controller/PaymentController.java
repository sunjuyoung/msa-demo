package sun.board.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {


    @PostMapping("/checkout")
    public String checkout() {
        log.info("Checkout process initiated");
        // Here you would typically call a service to handle the checkout logic
        return "Checkout successful";
    }
}
