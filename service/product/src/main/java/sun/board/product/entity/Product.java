package sun.board.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
    //사용자 명을 자주 조회한다면 memberName 컬럼을 추가할 수도 있습니다,
    //반정규화로 너무 잦은 memberService 호출을 피할 수 있습니다.




    public void minusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("감소할 재고는 0보다 작을 수 없습니다.");
        }
        if (this.stockQuantity < stockQuantity) {
            throw new IllegalArgumentException("현재 재고보다 많은 수량을 감소시킬 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }

    public void plusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("증가할 재고는 0보다 작을 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity + stockQuantity;
    }

}