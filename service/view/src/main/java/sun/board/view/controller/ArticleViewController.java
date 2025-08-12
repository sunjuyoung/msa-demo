package sun.board.view.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.board.view.service.ArticleViewService;

@RestController
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    @PostMapping("/article-views/articles/{articleId}/users/{userId}")
    public Long increase(@PathVariable Long articleId, @PathVariable Long userId){
        return articleViewService.increase(articleId, userId);
    }

    @GetMapping("/article-views/articles/{articleId}/count")
    public Long count(@PathVariable Long articleId){
        return articleViewService.count(articleId);
    }

}
