package sun.board.hotarticle.service.eventHandler;

import sun.board.common.event.Event;
import sun.board.common.event.EventType;
import sun.board.common.event.payload.CommentDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sun.board.hotarticle.repository.ArticleCommentCountRepository;
import sun.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload>{

    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        CommentDeletedEventPayload payload = event.getPayload();

        articleCommentCountRepository.createOrUpdate(
             payload.getArticleId(),
             payload.getArticleCommentCount(),
             TimeCalculatorUtils.calculateDurationToMidnight()
        );


    }

    @Override
    public boolean supports(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED == event.getType();
    }

    @Override
    public Long findArticleId(Event<CommentDeletedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
