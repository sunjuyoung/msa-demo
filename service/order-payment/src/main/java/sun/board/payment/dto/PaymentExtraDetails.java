package sun.board.payment.dto;


import lombok.*;
import sun.board.payment.entity.enums.PSPConfirmStatus;
import sun.board.payment.entity.enums.PaymentMethod;
import sun.board.payment.entity.enums.PaymentType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class PaymentExtraDetails {

    private PaymentType type;
    private PaymentMethod method;
    private LocalDateTime approvedAt;
    private String orderName;
    private PSPConfirmStatus pspConfirmStatus;
    private Long totalAmount;
    private String pspRawData; //승인결과
}