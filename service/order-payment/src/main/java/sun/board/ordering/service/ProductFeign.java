package sun.board.ordering.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.board.ordering.dto.ProductDto;
import sun.board.ordering.dto.ProductUpdateStockDto;

//name 은 eureka 서비스 이름
@FeignClient(name="product-service")
public interface ProductFeign {


    @GetMapping("/product/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    @PutMapping("/product/decreaseStock")
    Long decreaseStock(@RequestBody ProductUpdateStockDto productUpdateStockDto);
}
