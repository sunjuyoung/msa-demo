package sun.board.view.outbox;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sun.board.view.event.EventType;

import java.time.LocalDateTime;

@Table(name = "outbox")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutBox {
    @Id
    private Long outboxId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String payload;
    private LocalDateTime createdAt;

    public static OutBox create(Long outboxId, EventType eventType, String payload) {
        OutBox outBox = new OutBox();
        outBox.outboxId = outboxId;
        outBox.eventType = eventType;
        outBox.payload = payload;
        outBox.createdAt = LocalDateTime.now();
        return outBox;
    }
}
