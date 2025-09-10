package sun.board.view.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.board.view.event.payload.ArticleViewedEventPayload;
import sun.board.view.event.payload.ProductViewedEventPayload;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {

    PRODUCT_VIEWED(ProductViewedEventPayload.class,Topic.PRODUCT_VIEW),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class,Topic.ARTICLE_VIEW);


    private final Class<? extends EventPayload> payloadClass;
    private final String topic;




    public static class Topic{
        public static final String ARTICLE_VIEW = "article-view";
        public static final String PRODUCT_VIEW = "product-view";
    }
}
