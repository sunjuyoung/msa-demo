package sun.board.payment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import sun.board.payment.entity.enums.PaymentOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_order")
public class PaymentOrder  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String orderKey;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus paymentOrderStatus; //not_started, executing, success, failure


    private boolean isLedgerUpdated;
    private boolean isWalletUpdated;
    private boolean isPaymentDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_event_id")
    private PaymentEvent paymentEvent;

    @Column(name = "product_id")
    private Long productId; //상품 ID




    private int failedCount; //실패 횟수
    private int threshold; //실패 임계치


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }

    public void setPaymentStatus(PaymentOrderStatus paymentOrderStatus) {
        this.paymentOrderStatus = paymentOrderStatus;
    }
}
