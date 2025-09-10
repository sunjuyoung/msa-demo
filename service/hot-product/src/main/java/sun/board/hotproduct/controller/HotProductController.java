package sun.board.hotproduct.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.board.hotproduct.dto.ProductResponse;
import sun.board.hotproduct.service.HotProductService;
import sun.board.product.grpc.GetProductResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotProductController {

    private final HotProductService hotProductService;

    //날짜별 핫 상품 리스트 조회
    @GetMapping("/hot-products/date/{dateString}")
    public List<ProductResponse> getHotProductsByDate(@PathVariable("dateString") String dateString){
       return  hotProductService.readAll(dateString);
    }
}
