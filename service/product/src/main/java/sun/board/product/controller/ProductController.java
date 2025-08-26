package sun.board.product.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.dto.request.ProductCreateRequest;
import sun.board.product.dto.request.ProductSearchRequest;
import sun.board.product.dto.response.ProductDetailResponse;
import sun.board.product.dto.response.ProductResponse;
import sun.board.product.dto.response.list.PageResult;
import sun.board.product.dto.response.list.ProductListItemResponse;
import sun.board.product.entity.enums.ProductCategory;
import sun.board.product.entity.enums.ProductColor;
import sun.board.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity< PageResult<ProductListItemResponse>  > getProductList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<ProductColor> colors,
            @RequestParam(required = false) List<Integer> sizes,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        ProductSearchRequest searchRequest = ProductSearchRequest.builder()
                .name(name)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .colors(colors)
                .sizes(sizes)
                .inStock(inStock)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResult<ProductListItemResponse> productList = productService.getProductList(searchRequest);
        return ResponseEntity.ok(productList);
    }



    @PostMapping("/create")
    public ResponseEntity<?> productCreate(@RequestBody ProductCreateRequest dto, @RequestHeader("X-User-Id") String userId) {
        Long id = productService.ProductCreateAndReturnId(dto,userId);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> productDetail(@PathVariable("id") Long id)  {
        // Thread.sleep(4000L);
        ProductDetailResponse dto = productService.productDetail(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity< List<ProductResponse>> productList() {
        List<ProductResponse> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

//
//
    @PutMapping("/decreaseStock")
    public ResponseEntity<Long> productDecreaseStock(@RequestBody ProductUpdateStockDto dto) {
        Long id = productService.productMinusStock(dto);
        return new ResponseEntity<>( id, HttpStatus.OK);
    }

//    @PutMapping("/increaseStock")
//    public ResponseEntity<Long> productIncreaseStock(@RequestBody ProductUpdateStockDto dto) {
//        Product product = productService.productPlusStock(dto);
//        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
//    }
}
