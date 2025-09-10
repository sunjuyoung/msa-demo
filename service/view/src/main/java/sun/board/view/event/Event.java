package sun.board.view.event;

import lombok.Getter;

@Getter
public class Event <T extends EventPayload>{

    private Long eventId;
    private EventType type;
    private T payload;

    public static Event<EventPayload> of(Long eventId, EventType type, EventPayload payload){
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.type = type;
        event.payload = payload;
        return event;
    }

    public String to_json(){
        return DataSerializer.serialize(this);
    }

}
