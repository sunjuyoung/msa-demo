package sun.board.like.service.response;

import lombok.Getter;
import lombok.ToString;
import sun.board.like.entity.Likes;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleLikeResponse {

    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;

    public static ArticleLikeResponse from(Likes likes){
        ArticleLikeResponse articleLikeResponse = new ArticleLikeResponse();
        articleLikeResponse.articleLikeId = likes.getLikeId();
        articleLikeResponse.articleId = likes.getTargetId();
        articleLikeResponse.userId = likes.getUserId();
        articleLikeResponse.createdAt = likes.getCreatedAt();
        return articleLikeResponse;
    }



}
