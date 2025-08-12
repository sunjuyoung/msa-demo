package sun.board.payment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import sun.board.payment.entity.enums.PaymentMethod;
import sun.board.payment.entity.enums.PaymentType;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_event")
public class PaymentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    private String orderKey;

    @Column(name = "customer_id")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(unique = true)
    private String paymentKey;

    private String orderName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "psp_raw_data", columnDefinition = "json")
    private String pspRawData;  //PSP 로 부터 받은 원시 데이터

    @Column(name = "approved_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime approvedAt;


    private boolean isPaymentDone;

    private BigDecimal totalAmount;


    @Builder.Default
    @OneToMany(mappedBy = "paymentEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentOrder> paymentOrders = new ArrayList<>();


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addPaymentOrder(PaymentOrder paymentOrder) {
        paymentOrders.add(paymentOrder);
        paymentOrder.setPaymentEvent(this);
    }


}
