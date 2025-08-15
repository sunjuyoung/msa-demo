package sun.board.payment.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.board.payment.dto.Failure;
import sun.board.payment.entity.enums.PaymentOrderStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmResult {

    private PaymentOrderStatus status;
    private Failure failure;
    private String message;



    public void isValidMessage(){
        if(status == PaymentOrderStatus.SUCCESS){
            message = "결제 처리에 성공하였습니다.";
        }else if(status == PaymentOrderStatus.FAILURE) {
            message = "결제 처리에 실패하였습니다.";
        }
    }

}
