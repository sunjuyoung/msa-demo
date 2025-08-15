package sun.board.product.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.board.product.dto.ProductRegisterDto;
import sun.board.product.dto.ProductResDto;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.entity.Product;
import sun.board.product.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> productCreate(ProductRegisterDto dto, @RequestHeader("X-User-Id") String userId) {
        Product product = productService.productCreate(dto,userId);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResDto> productDetail(@PathVariable("id") Long id)  {
        // Thread.sleep(4000L);
        ProductResDto dto = productService.productDetail(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @PutMapping("/decreaseStock")
    public ResponseEntity<Long> productDecreaseStock(@RequestBody ProductUpdateStockDto dto) {
        Product product = productService.productMinusStock(dto);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }

    @PutMapping("/increaseStock")
    public ResponseEntity<Long> productIncreaseStock(@RequestBody ProductUpdateStockDto dto) {
        Product product = productService.productPlusStock(dto);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }
}
