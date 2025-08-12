package sun.board.articleread.service.response;

import lombok.Getter;
import lombok.ToString;
import sun.board.articleread.repository.ArticleQueryModel;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleReadResponse {

    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private Long articleLikeCount;
    private Long articleCommentCount;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;
    private Long articleViewCount;

    public static ArticleReadResponse from(ArticleQueryModel articleQueryModel, Long articleViewCount){
        ArticleReadResponse articleReadResponse = new ArticleReadResponse();
        articleReadResponse.articleId = articleQueryModel.getArticleId();
        articleReadResponse.title = articleQueryModel.getTitle();
        articleReadResponse.content = articleQueryModel.getContent();
        articleReadResponse.boardId = articleQueryModel.getBoardId();
        articleReadResponse.writerId = articleQueryModel.getWriterId();
        articleReadResponse.articleLikeCount = articleQueryModel.getArticleLikeCount();
        articleReadResponse.articleCommentCount = articleQueryModel.getArticleCommentCount();
        articleReadResponse.modifiedAt = articleQueryModel.getModifiedAt();
        articleReadResponse.createdAt = articleQueryModel.getCreatedAt();
        articleReadResponse.articleViewCount = articleViewCount;
        return articleReadResponse;
    }
}
