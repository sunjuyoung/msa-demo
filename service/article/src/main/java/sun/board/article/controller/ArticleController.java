package sun.board.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import sun.board.article.entity.Article;
import sun.board.article.service.ArticleService;
import sun.board.article.service.request.ArticleCreatRequest;
import sun.board.article.service.request.ArticleUpdateRequest;
import sun.board.article.service.response.ArticlePageResponse;
import sun.board.article.service.response.ArticleResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId){
        return articleService.read(articleId);
    }

    @GetMapping("/articles/boards/{boardId}/count")
    public Long count(@PathVariable Long boardId){
        return articleService.count(boardId);
    }


    @GetMapping("/articles")
    public ArticlePageResponse readAll(@RequestParam Long boardId, @RequestParam Long page, @RequestParam Long pageSize){
        return articleService.readAll(boardId, page, pageSize);
    }

    @PostMapping("/articles")
    public ArticleResponse create(@RequestBody ArticleCreatRequest request){
        return articleService.create(request);
    }

    @PutMapping("/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request){
        return articleService.update(articleId,request);
    }
    @DeleteMapping("/articles/{articleId}")
    public void delete(@PathVariable Long articleId){
        articleService.delete(articleId);
    }

//    @GetMapping("/v1/articles/query")
//    public ArticlePageResponse readAllQuery(@RequestParam Long boardId, @RequestParam int page, @RequestParam int pageSize){
//        Pageable pageable = PageRequest.of(page-1,pageSize);
//        return articleService.readAllQuerydsl(boardId,pageable);
//    }
//    @GetMapping("/v1/articles/query2")
//    public Page<Article> readAllQuery2(@RequestParam Long boardId, @RequestParam int page, @RequestParam int pageSize){
//        Pageable pageable = PageRequest.of(page-1,pageSize);
//        return articleService.readAllQuerydsl2(boardId,pageable);
//    }

    @GetMapping("/articles/infinite-scroll")
    public List<ArticleResponse> infiniteScroll(@RequestParam Long boardId,
                                                @RequestParam(required = false) Long lastArticleId,
                                                 @RequestParam Long pageSize
    ){

    return articleService.readAllInfiniteScroll(boardId, pageSize,lastArticleId );
    }


}
