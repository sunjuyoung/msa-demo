package sun.board.hotarticle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.board.hotarticle.service.HotArticleService;
import sun.board.hotarticle.service.response.HotArticleResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotArticleController {

    private final HotArticleService hotArticleService;

    @GetMapping("/hot-articles/articles/date/{dateStr}")
    public List<HotArticleResponse> readAll(@PathVariable String dateStr){
        return hotArticleService.readAll(dateStr);
    }
}
