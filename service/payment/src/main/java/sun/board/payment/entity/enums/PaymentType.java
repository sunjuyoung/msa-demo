package sun.board.payment.entity.enums;

import lombok.Getter;

@Getter
public enum PaymentType {


    NORMAL("일반 결제"),


    TEST_PAYMENT("테스트 결제");




    private String description;

    private PaymentType(String description) {
        this.description = description;
    }

    public static PaymentType getPaymentType(String description){
        return description.equals("일반 결제") ? PaymentType.NORMAL : null;
    }
}
