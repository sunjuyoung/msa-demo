package sun.board.articleread.repository;

import lombok.Getter;
import sun.board.articleread.client.ArticleClient;
import sun.board.common.event.payload.*;

import java.time.LocalDateTime;

@Getter
public class ArticleQueryModel {

    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private Long articleLikeCount;
    private Long articleCommentCount;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    public static ArticleQueryModel create(ArticleCreatedEventPayload payload) {
        ArticleQueryModel article = new ArticleQueryModel();
        article.articleId = payload.getArticleId();
        article.title = payload.getTitle();
        article.content = payload.getContent();
        article.boardId = payload.getBoardId();
        article.writerId = payload.getWriterId();
        article.createdAt = payload.getCreatedAt();
        article.modifiedAt = payload.getModifiedAt();
        article.articleLikeCount = 0L;
        article.articleCommentCount = 0L;
        return article;
    }

    //레디스에 데이터가 없을때 직접 api호출로 데이터를 가져옴
    public static ArticleQueryModel create(ArticleClient.ArticleResponse article, Long commentCount, Long articleLikeCount) {
        ArticleQueryModel articleQueryModel = new ArticleQueryModel();
        articleQueryModel.articleId = article.getArticleId();
        articleQueryModel.title = article.getTitle();
        articleQueryModel.content = article.getContent();
        articleQueryModel.boardId = article.getBoardId();
        articleQueryModel.writerId = article.getWriterId();
        articleQueryModel.createdAt = article.getCreatedAt();
        articleQueryModel.modifiedAt = article.getUpdatedAt();
        articleQueryModel.articleLikeCount = articleLikeCount;
        articleQueryModel.articleCommentCount = commentCount;
        return articleQueryModel;
    }


    public void updateBy(CommentCreatedEventPayload payload) {
       this.articleCommentCount = payload.getArticleCommentCount();
    }

    public void updateBy(ArticleLikedEventPayload payload) {
        this.articleLikeCount = payload.getArticleLikeCount();
    }

    public void updateBy(ArticleUnlikedEventPayload payload) {
        this.articleLikeCount = payload.getArticleLikeCount();
    }

    public void updateBy(CommentDeletedEventPayload payload) {
        this.articleCommentCount = payload.getArticleCommentCount();
    }


    public void updateBy(ArticleUpdatedEventPayload payload) {
        this.title = payload.getTitle();
        this.content = payload.getContent();
        this.boardId = payload.getBoardId();
        this.writerId = payload.getWriterId();
        this.createdAt = payload.getCreatedAt();
        this.modifiedAt = payload.getModifiedAt();

    }
}
