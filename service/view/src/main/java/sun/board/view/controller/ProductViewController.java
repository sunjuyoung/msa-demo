package sun.board.view.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sun.board.view.service.ProductViewService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product-views")
public class ProductViewController {

    private final ProductViewService productViewService;

    @PostMapping("/product/{productOptionId}/users/{userId}")
    public Long increase(@PathVariable Long productOptionId, @PathVariable Long userId){
        return productViewService.increase(productOptionId, userId);
    }

    @GetMapping("/product/{productOptionId}/count")
    public Long count(@PathVariable Long productOptionId){
        return productViewService.count(productOptionId);
    }


}
