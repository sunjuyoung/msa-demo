package sun.board.articleread.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.board.articleread.client.ArticleClient;
import sun.board.articleread.client.CommentClient;
import sun.board.articleread.client.LikeClient;
import sun.board.articleread.client.ViewClient;
import sun.board.articleread.repository.ArticleQueryModel;
import sun.board.articleread.repository.ArticleQueryModelRepository;
import sun.board.articleread.service.event.handler.EventHandler;
import sun.board.articleread.service.response.ArticleReadResponse;
import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReadService {

    private final ArticleClient articleClient;
    //private final CommentClient commentClient;
    private final LikeClient likeClient;
    private final ViewClient viewClient;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final List<EventHandler> eventHandlers;


    public void handlEvent(Event<EventPayload> event){
        for(EventHandler eventHandler : eventHandlers){
            if(eventHandler.supports(event)){
                eventHandler.handle(event);
            }
        }
    }


    public ArticleReadResponse read(Long articleId){
        ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
                .or(() -> fetch(articleId))
                .orElseThrow();

        return ArticleReadResponse.from(
                articleQueryModel,
                viewClient.count(articleId)

        );
    }

    private Optional<ArticleQueryModel> fetch(Long articleId) {
        Optional<ArticleQueryModel> articleQueryModelOptional = articleClient.read(articleId)
                .map(article -> ArticleQueryModel.create(
                        article,
                      //  commentClient.count(articleId),
                        0L,
                        likeClient.count(articleId)
                ));
        articleQueryModelOptional
                .ifPresent(articleQueryModel -> articleQueryModelRepository.create(articleQueryModel, Duration.ofDays(1)));
        log.info("[ArticleReadService.fetch] fetch data. articleId={}, isPresent={}", articleId, articleQueryModelOptional.isPresent());
        return articleQueryModelOptional;
    }


}
