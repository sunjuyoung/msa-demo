package sun.board.view.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import sun.board.common.snowflake.Snowflake;
import sun.board.view.event.Event;
import sun.board.view.event.EventPayload;
import sun.board.view.event.EventType;

@Component
@RequiredArgsConstructor
public class OutBoxEventPublisher {

    private final Snowflake outboxIdSnowflake = new Snowflake();
    private final Snowflake eventIdSnowflake = new Snowflake();
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType eventType, EventPayload eventPayload){

        OutBox outbox = OutBox.create(
                outboxIdSnowflake.nextId(),
                eventType,
                Event.of(
                        eventIdSnowflake.nextId(),
                        eventType,
                        eventPayload
                ).to_json()
        );
        applicationEventPublisher.publishEvent(outbox);
    }
}
