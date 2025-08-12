package sun.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import sun.board.comment.service.response.CommentResponse;

public class CommentApiTest {


    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create(){

        CommentResponse my_comment1 = createComment(new CommentCreateRequest("my comment1", null, 1L, 1L));
        CommentResponse my_comment2 = createComment(new CommentCreateRequest("my comment2", my_comment1.getCommentId(), 1L, 1L));
        CommentResponse my_comment3 = createComment(new CommentCreateRequest("my comment3", my_comment1.getCommentId(), 1L, 1L));
    }

    CommentResponse createComment(CommentCreateRequest request){
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }






    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private String content;
        private Long parentCommentId;
        private Long articleId;
        private Long writerId;
    }

}
