package sun.board.common.dataserializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSerializer {



    private static final ObjectMapper objectMapper = initialize();

    private static ObjectMapper initialize() {
     return  new ObjectMapper()
              .registerModule(new JavaTimeModule()) // (시간)LocalDateTime을 serialize/deserialize 하기 위한 설정
              //역 직렬화할때 없는 필드 있으면 에러가 날 수 있는 설정인데 이걸 false로 해두면 무시하고 진행한다.
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    //String data를 받아서 clazz로 deserialize(역직렬화)하는 메소드
    public static <T> T deserialize(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            log.error("Failed to deserialize json: {}", data, e);
            return null;
        }
    }

    //Object data를 받아서 clazz타입으로 변환
    public static <T> T deserialize(Object data, Class<T> clazz) {
     return objectMapper.convertValue(data, clazz);
    }

    public static String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Failed to serialize json: {}", data, e);
            return null;
        }
    }
}
