package sun.board.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sun.board.like.service.ArticleLikeService;
import sun.board.like.service.response.ArticleLikeResponse;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;


    @GetMapping("/likes/article/{articleId}/user/{userId}")
    public ArticleLikeResponse read(@PathVariable Long articleId, @PathVariable Long userId){
        return articleLikeService.read(articleId, userId);
    }

    @GetMapping("/likes/product/{productOptionId}/count")
    public Long count(@PathVariable Long articleId){
        return articleLikeService.count(articleId);
    }

    @PostMapping("/likes/product/{productOptionId}/user/{userId}/pessimistic-lock")
    public void like(@PathVariable Long productOptionId, @PathVariable Long userId){
        articleLikeService.likePessmisticLock(productOptionId, userId);
    }

    @DeleteMapping("/likes/product/{productOptionId}/user/{userId}/pessimistic-lock")
    public void unlike(@PathVariable Long productOptionId, @PathVariable Long userId){
        articleLikeService.unlikePessmisticLock(productOptionId, userId);
    }


    // 비관적 락을 사용한 좋아요 select ... for update + update
    @PostMapping("/likes/article/{articleId}/user/{userId}/pessimistic-lock2")
    public void like2(@PathVariable Long articleId, @PathVariable Long userId){
        articleLikeService.likePessmisticLock2(articleId, userId);
    }

    @DeleteMapping("/likes/article/{articleId}/user/{userId}/pessemistic-lock2")
    public void unlike2(@PathVariable Long articleId, @PathVariable Long userId){
        articleLikeService.unlikePessmisticLock2(articleId, userId);
    }


    @PostMapping("/likes/article/{articleId}/user/{userId}/optimistic-lock")
    public void like3(@PathVariable Long articleId, @PathVariable Long userId){
        articleLikeService.likeOptimisticLock(articleId, userId);
    }

    @DeleteMapping("/likes/article/{articleId}/user/{userId}/optimistic-lock")
    public void unlike3(@PathVariable Long articleId, @PathVariable Long userId){
        articleLikeService.unlikeOptimisticLock(articleId, userId);
    }
}
