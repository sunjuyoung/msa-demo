package sun.board.payment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.ordering.dto.ProductDto;
import sun.board.ordering.entity.Ordering;
import sun.board.ordering.repository.OrderingRepository;
import sun.board.ordering.service.ProductFeign;
import sun.board.payment.dto.request.CheckoutRequest;
import sun.board.payment.entity.PaymentEvent;
import sun.board.payment.entity.PaymentOrder;
import sun.board.payment.entity.enums.PaymentOrderStatus;
import sun.board.payment.repository.PaymentEventRepository;
import sun.board.payment.repository.PaymentOrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderingRepository orderRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private static final String TEST_ORDER_KEY = "_testNumber";
    private final ProductFeign productFeign;


    @Transactional
    public Long checkout(CheckoutRequest request){
        Long orderId = request.getOrderId();
        Long productId = request.getProductId();
        log.info("checkout orderId={}, productId={}", orderId, productId);

        //주문, 상품들 조회
        Ordering ordering =
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        ProductDto product = productFeign.getProductById(productId);

        PaymentEvent paymentEvent = createPaymentEvent(ordering, request,product);


        //CascadeType.ALL로 인해 PaymentEvent 저장시 PaymentOrder도 같이 저장
        PaymentEvent newPaymentEvent = paymentEventRepository.save(paymentEvent);

        log.info("paymentEvent={}", paymentEvent.getOrderId());
        return newPaymentEvent.getId();

    }


    private PaymentEvent createPaymentEvent(Ordering order, CheckoutRequest request, ProductDto product) {

        //PaymentEvent 생성
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .orderId(order.getId())
                .orderKey(request.getOrderId() + TEST_ORDER_KEY)
                .orderName(request.getOrderName())
                .totalAmount(request.getAmount())
                .build();

        //PaymentOrder 생성
        // List<PaymentOrder> paymentOrders = createPaymentOrders(order, request);
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .productId(product.getProductId())
                .orderKey(request.getOrderId() + TEST_ORDER_KEY)
                .amount(request.getAmount())
                .paymentOrderStatus(PaymentOrderStatus.NOT_STARTED)
                .build();

        //PaymentOrder에 PaymentEvent 설정
        paymentOrder.setPaymentEvent(paymentEvent);
        //paymentEvent.addPaymentOrder(paymentOrder);


        //PaymentEvent에 PaymentOrder 추가
//        paymentOrders.forEach(paymentOrder -> {
//            paymentEvent.addPaymentOrder(paymentOrder);
//        });

        return paymentEvent;

    }

//    private static List<PaymentOrder> createPaymentOrders(Order order, CheckoutRequest request) {
//
//        return order.getOrderItems().stream()
//                .map(o -> PaymentOrder.builder()
//                        .product(o.getProduct())
//                        .orderKey(request.getOrderId() + TEST_ORDER_KEY)
//                        .amount(o.getAmounts())
//                        .paymentOrderStatus(PaymentOrderStatus.NOT_STARTED)
//                        .build())
//                .collect(Collectors.toList());
//    }
}
