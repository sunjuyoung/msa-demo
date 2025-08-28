package sun.board.payment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.board.payment.entity.PaymentEvent;
import sun.board.payment.entity.enums.PaymentMethod;
import sun.board.payment.entity.enums.PaymentType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

        @EntityGraph(attributePaths = "paymentOrders")
        Optional<PaymentEvent> findAllByOrderKey(String key);

        @Modifying
        @Query("update PaymentEvent p set p.paymentKey = :paymentKey where p.orderKey = :orderKey")
        void updatePaymentKeyByOrderId(@Param("paymentKey") String paymentKey, @Param("orderKey") String orderKey);

        // update_payment_event_extra_details
        @Modifying(flushAutomatically = true, clearAutomatically = true)
        @Query("update PaymentEvent p " +
                        " set p.orderName = :orderName, p.paymentMethod = :method, p.approvedAt =:approvedAt, p.paymentType =:type, p.pspRawData =:pspRawData, p.updatedAt = CURRENT_TIMESTAMP"
                        +
                        " where p.orderKey = :orderKey")
        void updatePaymentEventExtraDetails(@Param("orderKey") String orderKey,
                        @Param("orderName") String orderName,
                        @Param("method") PaymentMethod method,
                        @Param("approvedAt") LocalDateTime approvedAt,
                        @Param("type") PaymentType type,
                        @Param("pspRawData") String pspRawData);

        // 결제 처리 중인 결제건을 조회
        @Query("select pe.id as payment_event_id, pe.paymentKey, pe.orderKey, po.id as payment_order_id, po.paymentOrderStatus, po.amount ,po.failedCount,po.threshold "
                        +
                        " from  PaymentEvent pe join pe.paymentOrders po " +
                        " where po.paymentOrderStatus = 'EXECUTING' and  po.updatedAt <= :thresholdTime and po.failedCount < 3") // 3분 전 결제건
        Optional<PaymentEvent> getPendingPaymentEvent(@Param("thresholdTime") LocalDateTime thresholdTime);

}
