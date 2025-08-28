package sun.board.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.board.payment.entity.PaymentOrder;
import sun.board.payment.entity.enums.PaymentOrderStatus;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {


    @Modifying
    @Query("update PaymentOrder p set p.paymentOrderStatus =:status, p.updatedAt = CURRENT_TIMESTAMP" +
            " where p.orderKey = :orderKey")
    void updatePaymentOrderStatusByOrderId(@Param("orderKey") String orderKey, @Param("status") PaymentOrderStatus status);
}
