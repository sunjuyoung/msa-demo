package sun.board.articleread.service.event.handler;

import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;

public interface EventHandler <T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
