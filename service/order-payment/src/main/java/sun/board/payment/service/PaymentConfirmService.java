package sun.board.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.board.payment.dto.PaymentStatusUpdateCommand;
import sun.board.payment.dto.request.PaymentConfirmCommand;
import sun.board.payment.dto.request.PaymentExecutionResult;
import sun.board.payment.dto.response.PaymentConfirmResult;
import sun.board.payment.entity.PaymentEvent;
import sun.board.payment.entity.enums.PaymentOrderStatus;
import sun.board.payment.exception.ex.PaymentAmountException;
import sun.board.payment.repository.PaymentEventRepository;
import sun.board.payment.repository.PaymentOrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConfirmService {

    //## 결제 승인 기능
    //결제 창에서 결제 정보를 입력하고 인증한 후에 결제서버측에서 psp로 결제 승인을 보내는 과정
    //
    //1. 유저가 결제 승인요청
    //2. Payment 의 상태를 NOT_STARTED EXECUTING 상태로 변경
    //   (장애로 인해 결제가 실패할경우  EXECUTING (인증이완료) 은 복구서비스 통해서 완료시도)
    //4. 결제에 대한 유효성 검사 . (e.g 금액 등 )
    //5. PSP 에 결제 승인을 요청한다 .
    //6. PSP 결제 승인 결과에 따라서 결제 완료 / 실패 상태를 저장
    //7.  결제 승인 결과를 사용자에게 전달한다 .

    private final PaymentEventRepository paymentEventRepository;
    private final TossPaymentService tossPaymentService;

    private final ObjectMapper objectMapper;

    private final PaymentOrderRepository paymentOrderRepository;

    //private final PaymentOrderStatusService paymentOrderStatusService;

    @Transactional
    public PaymentConfirmResult confirm(PaymentEvent paymentEvent, PaymentConfirmCommand command) {

    //    PaymentEvent paymentEvent = paymentEventRepository.findAllByOrderKey(command.getOrderId()).orElseThrow();
    //    List<PaymentOrder> paymentOrders = paymentEvent.getPaymentOrders();

        //PaymentOrderStatus 변경 NOT_STARTED -> EXECUTING
        //가장 먼저 EXECUTING 상태 변경을 통해 , 장애 발생시 복구 서비스를 통해 결제를 완료할 수 있도록 한다.
       // paymentOrderStatusService.updatePaymentOrderStatus(paymentOrders);

        //paymentKey 업데이트
      //  paymentEventRepository.updatePaymentKeyByOrderId(command.getPaymentKey(), command.getOrderId());

        //결제 금액 검증
        isAmountValid(command, paymentEvent);

        PaymentExecutionResult res = tossPaymentService.execute(command).block();


        //결제 승인 결과 따른 저장
        PaymentStatusUpdateCommand paymentStatusUpdateCommand
                = PaymentStatusUpdateCommand.builder()
                .paymentKey(res.getPaymentKey())
                .orderId(res.getOrderId())
                .status(res.paymentOrderStatus())
                .extraDetails(res.getExtraDetails())
                .failure(res.getFailure())
                .build();

        //결제 상태 업데이트, psprawdata 저장
        updatePaymentStatus(paymentStatusUpdateCommand);

        PaymentConfirmResult confirmResult = PaymentConfirmResult.builder()
                .status(res.paymentOrderStatus())
                .failure(res.getFailure())
                .build();
        confirmResult.isValidMessage();
        return confirmResult;
    }



    public boolean updatePaymentStatus(PaymentStatusUpdateCommand command){
        if(command.getStatus().equals(PaymentOrderStatus.SUCCESS)) {
            return updatePaymentStatusToSuccess(command);
        }else if(command.getStatus().equals(PaymentOrderStatus.FAILURE)){
            return updatePaymentStatusToFailure(command);
        }else {
            throw new IllegalArgumentException("결제 상태가 올바르지 않습니다, 상태:"+ command.getStatus());
        }

    }

    public boolean updatePaymentStatusToSuccess(PaymentStatusUpdateCommand command){
        //select paymentOrder
        //List<PaymentOrder> paymentOrderList = paymentOrderRepository.findByOrderId(command.getOrderId());
        //history 저장
//        insertPaymentHistory(paymentOrderList, command.getStatus(),"PAYMENT_CONFIRMATION_DONE");
        //update paymentOrder status
        paymentOrderRepository.updatePaymentOrderStatusByOrderId(command.getOrderId(), command.getStatus());

        String pspRawData = "";
        try {
            pspRawData = objectMapper.writeValueAsString(command.getExtraDetails().getPspRawData());
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        //paymentEvent
        paymentEventRepository.updatePaymentEventExtraDetails(command.getOrderId(),
                command.getExtraDetails().getOrderName(),
                command.getExtraDetails().getMethod(),
                command.getExtraDetails().getApprovedAt(),
                command.getExtraDetails().getType(),
                pspRawData);
        return true;

    }

    public boolean updatePaymentStatusToFailure(PaymentStatusUpdateCommand command) {
//        List<PaymentOrder> paymentOrderList = paymentOrderRepository.findByOrderId(command.getOrderId());
//        insertPaymentHistory(paymentOrderList, command.getStatus(),command.getStatus().toString());
        paymentOrderRepository.updatePaymentOrderStatusByOrderId(command.getOrderId(), command.getStatus());
        return true;
    }

    private static void isAmountValid(PaymentConfirmCommand command, PaymentEvent paymentEvent) {
        int compareResult = paymentEvent.getTotalAmount().compareTo(BigDecimal.valueOf(command.getAmount()));
        if(compareResult != 0){
            throw new PaymentAmountException(
                    " : 결제 금액이 일치하지 않습니다. 금액: " + command.getAmount() + "원, 결제 금액: " + paymentEvent.getTotalAmount() + "원\"");
        }
    }


//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public  void updatePaymentOrderStatus(List<PaymentOrder> paymentOrders) {
//        paymentOrders.stream().forEach(paymentOrder -> {
//            if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.NOT_STARTED ||
//                    paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.EXECUTING){
//                paymentOrder.setPaymentStatus(PaymentOrderStatus.EXECUTING);
//            }else  if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.SUCCESS) {
//                throw new PaymentAlreadyException(ExceptionCode.PAYMENT_ALREADY_COMPLETE);
//            } else if(paymentOrder.getPaymentOrderStatus() == PaymentOrderStatus.FAILURE){
//                throw new PaymentAlreadyException(ExceptionCode.PAYMENT_ALREADY_FAIL);
//            }
//        });
//    }


}
