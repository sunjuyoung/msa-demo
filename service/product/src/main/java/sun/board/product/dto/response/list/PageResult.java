package sun.board.product.dto.response.list;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@Builder
public class PageResult<T> {

    public final List<T> content;
    public final int total;       // 총 상품 수
    public final int page;        // 1-based
    public final int size;

    public PageResult(List<T> content, int total, int page, int size) {
        this.content = content;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
