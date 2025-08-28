package sun.board.payment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sun.board.payment.dto.request.PaymentConfirmCommand;
import sun.board.payment.dto.response.PaymentConfirmResult;
import sun.board.payment.entity.PaymentEvent;
import sun.board.payment.entity.PaymentOrder;
import sun.board.payment.repository.PaymentEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentConfirmFacade {

    private final PaymentConfirmService paymentConfirmService;
    private final PaymentOrderStatusService paymentOrderStatusService;
    private final PaymentEventRepository paymentEventRepository;


    public PaymentConfirmResult confirmPayment(PaymentConfirmCommand command) {
        PaymentEvent paymentEvent = paymentEventRepository.findAllByOrderKey(command.getOrderId()).orElseThrow();
        List<PaymentOrder> paymentOrders = paymentEvent.getPaymentOrders();

        //PaymentOrderStatus 변경 NOT_STARTED -> EXECUTING
        //paymentKey 업데이트
        paymentOrderStatusService.updatePaymentOrderStatus(paymentOrders,command);

        return paymentConfirmService.confirm(paymentEvent, command);
    }
}
