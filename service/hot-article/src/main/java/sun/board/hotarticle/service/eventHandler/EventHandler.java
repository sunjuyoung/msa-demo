package sun.board.hotarticle.service.eventHandler;

import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;

public interface EventHandler <T extends EventPayload> {

    void handle(Event<T> event); //처리 로직
    boolean supports(Event<T> event); //해당 이벤트를 처리할 수 있는지 여부
    Long findArticleId(Event<T> event); //이벤트에서 articleId 추출
}
