package sun.board.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import sun.board.common.event.EventType;
import sun.board.common.event.payload.ArticleCreatedEventPayload;
import sun.board.common.event.payload.ArticleDeletedEventPayload;
import sun.board.common.event.payload.ArticleUpdatedEventPayload;
import sun.board.common.outboxmessagerelay.OutboxEventPublisher;
import sun.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.article.entity.Article;
import sun.board.article.entity.BoardArticleCount;
import sun.board.article.repository.ArticleRepository;
import sun.board.article.repository.BoardArticleCountRepository;
import sun.board.article.service.request.ArticleCreatRequest;
import sun.board.article.service.request.ArticleUpdateRequest;
import sun.board.article.service.response.ArticlePageResponse;
import sun.board.article.service.response.ArticleResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    private final OutboxEventPublisher outboxEventPublisher;


    @Transactional
    public ArticleResponse create(ArticleCreatRequest request){

        Article article = Article.create(
                snowflake.nextId(),
                request.getTitle(),
                request.getContent(),
                request.getBoardId(),
                request.getWriterId()
        );
        Article saveArticle = articleRepository.save(article);

        int result = boardArticleCountRepository.increaseArticleCount(request.getBoardId());
        if(result == 0){
            boardArticleCountRepository.save(BoardArticleCount.init(request.getBoardId(), 1L));
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_CREATED,
                ArticleCreatedEventPayload.builder()
                        .articleId(saveArticle.getArticleId())
                        .boardId(saveArticle.getBoardId())
                        .writerId(saveArticle.getWriterId())
                        .content(saveArticle.getContent())
                        .title(saveArticle.getTitle())
                        .createdAt(saveArticle.getCreatedAt())
                        .modifiedAt(saveArticle.getUpdatedAt())
                        .boardArticleCount(count(saveArticle.getBoardId()))
                        .build(),
                saveArticle.getBoardId()
        );

        return ArticleResponse.from(saveArticle);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request){

        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());
        outboxEventPublisher.publish(
                EventType.ARTICLE_UPDATED,
                ArticleUpdatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .content(article.getContent())
                        .title(article.getTitle())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getUpdatedAt())
                        .build(),
                article.getBoardId()
        );
        return ArticleResponse.from(article);

    }

    public ArticleResponse read(Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow();
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);

        boardArticleCountRepository.decreaseArticleCount(article.getBoardId());
        outboxEventPublisher.publish(
                EventType.ARTICLE_DELETED,
                ArticleDeletedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .content(article.getContent())
                        .title(article.getTitle())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getUpdatedAt())
                        .build(),
                article.getBoardId()
        );
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize){
        List<ArticleResponse> articleResponses = articleRepository.findAll(boardId, (page - 1) * pageSize, pageSize)
                .stream()
                .map(ArticleResponse::from)
                .toList();

        Long count = articleRepository.count(boardId, PageCalculator.calculatePage(page, pageSize, 10L));

        return ArticlePageResponse.of(articleResponses, count);
    }

//    public ArticlePageResponse readAllQuerydsl(Long boardId, Pageable pageable){
//        List<ArticleResponse> articleResponses = articleRepository.readAllPage(boardId, pageable)
//                .stream()
//                .map(ArticleResponse::from)
//                .toList();
//
//        Long count = articleRepository.count(boardId, PageCalculator.calculatePage(
//                Long.valueOf(pageable.getPageNumber()), Long.valueOf(pageable.getPageSize()), 10L));
//
//
//        return ArticlePageResponse.of(articleResponses, count);
//
//    }
//
//    public Page<Article> readAllQuerydsl2(Long boardId, Pageable pageable){
//
//        Page<Article> allTest = articleRepository.findAllTest(boardId, pageable);
//
//        return allTest;
//
//    }

    public List<ArticleResponse> readAllInfiniteScroll(Long boardId,Long pageSize, Long lastArticleId){

        List<Article> articles =  lastArticleId == null ?
                articleRepository.findAllInfiniteScroll(boardId, pageSize) :
                articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);

        return articles.stream().map(ArticleResponse::from).toList();

    }

    public Long count (long boardId){
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount).orElse(0L);
    }
}
