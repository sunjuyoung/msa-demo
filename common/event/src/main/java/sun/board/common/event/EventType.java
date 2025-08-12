package sun.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.board.common.event.payload.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {

    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.SUN_BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.SUN_BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.SUN_BOARD_ARTICLE),
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.SUN_BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.SUN_BOARD_COMMENT),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.SUN_BOARD_VIEW),
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.SUN_BOARD_LIKE),
    ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.SUN_BOARD_LIKE);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type){
       try{
            return valueOf(type);
       }catch (Exception e){
           log.error("Failed to get EventType from {}", type, e);
           return null;
       }
    }

    public static class Topic{
        public static final String SUN_BOARD_ARTICLE = "sun-board-article";
        public static final String SUN_BOARD_COMMENT = "sun-board-comment";
        public static final String SUN_BOARD_LIKE = "sun-board-like";
        public static final String SUN_BOARD_VIEW = "sun-board-view";
    }

}
