package sun.board.hotproduct.service.eventHandler;

import sun.board.hotproduct.event.Event;
import sun.board.hotproduct.event.EventPayload;

public interface EventHandler <T extends EventPayload>{

    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findProductId(Event<T> event);
}
