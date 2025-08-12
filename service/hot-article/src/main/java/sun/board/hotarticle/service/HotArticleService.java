package sun.board.hotarticle.service;

import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;
import sun.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.board.hotarticle.client.ArticleClient;
import sun.board.hotarticle.repository.HotArticleListRepository;
import sun.board.hotarticle.service.eventHandler.EventHandler;
import sun.board.hotarticle.service.response.HotArticleResponse;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {


    private final ArticleClient articleClient;
    private final List<EventHandler> eventHandlers; //eventHandler 구현체 같이 주입된다
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final HotArticleScoreUpdater hotArticleScoreUpdater;
    private final HotArticleListRepository hotArticleListRepository;

    //이벤트를 통해서 인기글 점수 계산, 인기글 아이디 저장
    //consumer에서 이벤트를 받아서 처리
    public void handleEvent(Event<EventPayload> event){
        EventHandler<EventPayload> eventHandler = findEventHandler(event);
        if(eventHandler == null){
            log.error("Unsupported event={}", event);
            return;
        }
        if(isArticleCreatedOrDeleted(event)){//생성 또는 삭제 이벤트 확인
            eventHandler.handle(event);
        }else {// 점수 업데이트
            hotArticleScoreUpdater.update(event,eventHandler);
        }
    }



    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElse(null)
        ;
    }

    private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
    }

    public List<HotArticleResponse> readAll(String dateStr){
        // yyyyMMdd
        return hotArticleListRepository.readAll(dateStr)
                .stream()
                .map(articleClient::read)
                .filter(Objects::nonNull)
                .map(HotArticleResponse::from)
                .toList();

    }
}
