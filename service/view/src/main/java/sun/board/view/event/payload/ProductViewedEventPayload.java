package sun.board.view.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sun.board.view.event.EventPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewedEventPayload implements EventPayload {

    private Long productOptionId;
    private Long productViewCount;
}
