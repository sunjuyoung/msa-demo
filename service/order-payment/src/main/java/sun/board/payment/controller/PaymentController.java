package sun.board.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.board.payment.dto.request.TossPaymentConfirmRequest;
import sun.board.payment.dto.response.ApiResponse;
import sun.board.payment.dto.response.PaymentConfirmResult;

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

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResult>> confirmPayment(
            @RequestBody TossPaymentConfirmRequest request) {
        log.info("Payment confirmation process initiated");
        // Here you would typically call a service to handle the payment confirmation logic

        return ResponseEntity.ok().body(new ApiResponse<>("success", HttpStatus.OK, null));
    }
}
