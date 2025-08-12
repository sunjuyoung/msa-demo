package sun.board.article.service.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticleCreatRequest {
    private String title;
    private String content;
    private Long writerId;
    private Long boardId;
}
