package sun.board.hotproduct.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sun.board.hotproduct.event.EventPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLikeEventPayload implements EventPayload {

    private Long productOptionId;
    private Long productViewCount;
}
