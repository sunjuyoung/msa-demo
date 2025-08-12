package sun.board.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sun.board.comment.service.CommentService;
import sun.board.comment.service.request.CommentCreateRequest;
import sun.board.comment.service.response.CommentPageResponse;
import sun.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(@PathVariable Long commentId){
        return commentService.read(commentId);
    }

    @PostMapping("/v1/comments")
    public CommentResponse create(@RequestBody CommentCreateRequest request){
        return commentService.create(request);
    }


    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable Long commentId){
        commentService.delete(commentId);
    }


    @GetMapping("/v1/comments")
    public CommentPageResponse readAll(
            @RequestParam Long articleId,
            @RequestParam Long page,
            @RequestParam Long pageSize){
        
        return commentService.readAll(articleId,page,pageSize);
    }


    @GetMapping("/v1/comments/infinite")
    public List<CommentResponse> readAll(
            @RequestParam Long articleId,
            @RequestParam(required = false) Long lastParentCommentId,
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam Long pageSize){
        return commentService.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);
    }

    @GetMapping("/v1/comments/articles/{articleId}/count")
    public Long count(@PathVariable Long articleId){
        return commentService.count(articleId);
    }
}
