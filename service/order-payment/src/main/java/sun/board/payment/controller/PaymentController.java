package sun.board.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.board.payment.dto.request.CheckoutRequest;
import sun.board.payment.dto.request.PaymentConfirmCommand;
import sun.board.payment.dto.request.TossPaymentConfirmRequest;
import sun.board.payment.dto.response.ApiResponse;
import sun.board.payment.dto.response.PaymentConfirmResult;
import sun.board.payment.service.CheckoutService;
import sun.board.payment.service.PaymentConfirmFacade;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {


    private final CheckoutService checkoutService;

    // private final PaymentConfirmService confirmService;

    private final PaymentConfirmFacade paymentConfirmFacade;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Long>> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        Long checkout = checkoutService.checkout(checkoutRequest);
        return ResponseEntity.ok().body(new ApiResponse<>("success", HttpStatus.OK, checkout));
    }

    @RequestMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResult>> confirmPayment(
            @RequestBody TossPaymentConfirmRequest request) {

        PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(

                request.getPaymentKey(),
                request.getOrderId(),
                request.getAmount()

        );

        // PaymentConfirmResult confirm = confirmService.confirm(paymentConfirmCommand);
        PaymentConfirmResult confirm = paymentConfirmFacade.confirmPayment(paymentConfirmCommand);

        return ResponseEntity.ok().body(new ApiResponse<>("success", HttpStatus.OK, confirm));
    }
}
