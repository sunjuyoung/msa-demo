package sun.board.hotproduct.event;

import lombok.Getter;

//이벤트 통신
//이벤트를 카프카로 전달할떄 json으로 변환하고  다시 역직렬화 할때 사용할 클래스
@Getter
public class Event <T extends EventPayload> {

    private Long eventId; //식별 아이디
    private  EventType type; //이벤트 타입
    private  T payload; //이벤트 페이로드


    public static Event<EventPayload> of(Long eventId, EventType type, EventPayload payload){
        Event<EventPayload> event = new Event<>();
        event.type = type;
        event.payload = payload;
        event.eventId = eventId;
        return event;
    }

    public String to_json(){
        return DataSerializer.serialize(this);
    }

    public static Event<EventPayload> fromJson(String json){
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if(eventRaw == null){
            return null;
        }
        Event<EventPayload> event = new Event<>();
        event.eventId = eventRaw.eventId;
        event.type = EventType.from(eventRaw.getType());
        event.payload = DataSerializer.deserialize(eventRaw.getPayload(), event.type.getPayloadClass());
        return event;
    }

    //payload이 어떤 클래스 타입인지 다르기때문에
    //일단 Object로 받기위해
    @Getter
    private static class EventRaw{
        private Long eventId;
        private String type;
        private Object payload;
    }
}
