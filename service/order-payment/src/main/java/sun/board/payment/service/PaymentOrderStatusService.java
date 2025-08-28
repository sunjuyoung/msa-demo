package sun.board.payment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.payment.dto.request.PaymentConfirmCommand;
import sun.board.payment.entity.PaymentOrder;
import sun.board.payment.entity.enums.PaymentOrderStatus;
import sun.board.payment.exception.ex.PaymentAlreadyException;
import sun.board.payment.repository.PaymentEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentOrderStatusService {

    private final PaymentEventRepository paymentEventRepository;


    @Transactional
    public  void updatePaymentOrderStatus(List<PaymentOrder> paymentOrders, PaymentConfirmCommand command) {
        updateStatus(paymentOrders);
        updatePaymentKey(command);
    }

    private void updatePaymentKey(PaymentConfirmCommand command) {
        paymentEventRepository.updatePaymentKeyByOrderId(command.getPaymentKey(), command.getOrderId());
    }

    private  void updateStatus(List<PaymentOrder> paymentOrders) {
        paymentOrders.stream().forEach(paymentOrder -> {
            if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.NOT_STARTED ||
                    paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.EXECUTING){
                paymentOrder.setPaymentStatus(PaymentOrderStatus.EXECUTING);
            }else  if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.SUCCESS) {
                throw new PaymentAlreadyException("이미 결제가 실패한 주문입니다.");
            } else if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.FAILURE){
                throw new PaymentAlreadyException("이미 결제가 실패한 주문입니다.");
            }
        });
    }


}
