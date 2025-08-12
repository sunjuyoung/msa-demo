package sun.board.common.event;

import sun.board.common.event.payload.ArticleCreatedEventPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventTest {

    @Test
    void serde(){

        ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .createdAt(null)
                .modifiedAt(null)
                .boardArticleCount(1L)
                .build();

        Event<EventPayload> event = Event.of(1234L, EventType.ARTICLE_CREATED, payload);

        //event -> json으로  변경
        String json = event.to_json();
        System.out.println("json = " + json);

        //json -> event로 변경
        Event<EventPayload> result = Event.fromJson(json);

        assertThat(result.getEventId()).isEqualTo(1234L);
        assertThat(result.getType()).isEqualTo(EventType.ARTICLE_CREATED);
        assertThat(result.getPayload()).isInstanceOf(ArticleCreatedEventPayload.class);



    }
}