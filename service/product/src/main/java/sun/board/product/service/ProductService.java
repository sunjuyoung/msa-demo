package sun.board.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.dto.request.ProductCreateRequest;
import sun.board.product.dto.request.ProductOptionCreateRequest;
import sun.board.product.dto.request.ProductSearchRequest;
import sun.board.product.dto.response.ProductDetailResponse;
import sun.board.product.dto.response.ProductOptionResponse;
import sun.board.product.entity.Product;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;
import sun.board.product.mapper.ProductMapper;
import sun.board.product.repository.ProductOptionRepository;
import sun.board.product.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductMapper productMapper;


    /**
     * 상품 검색 페이지
     * @param searchRequest
     * @return
     */
    public Page<ProductDetailResponse> getProductList(ProductSearchRequest searchRequest) {
        // 전체 개수 조회
        searchRequest.setOffset(searchRequest.getPage() * searchRequest.getSize());
        int totalCount = productMapper.countProducts(searchRequest);

        // 상품 리스트 조회 (이미 MyBatis에서 색상별로 그룹핑됨)
        List<ProductDetailResponse> products = productMapper.findProductsWithOptions(searchRequest);

        // 상품별로 옵션 그룹핑 처리
        Map<Long, ProductDetailResponse> productMap = new LinkedHashMap<>();

        for (ProductDetailResponse product : products) {
            Long productId = product.getProductId();

            if (!productMap.containsKey(productId)) {
                // 첫 번째로 발견된 상품인 경우
                productMap.put(productId, ProductDetailResponse.builder()
                        .productId(product.getProductId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .category(product.getCategory())
                        .options(new ArrayList<>())
                        .build());
            }

            // 옵션이 있는 경우 추가
            if (product.getOptions() != null && !product.getOptions().isEmpty()) {
                productMap.get(productId).getOptions().addAll(product.getOptions());
            }
        }

        List<ProductDetailResponse> result = new ArrayList<>(productMap.values());

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        return new PageImpl<>(result, pageable, totalCount);
    }



    /**
     * 상품 리스트
     */
    public List<ProductDetailResponse> getProducts() {

        // 1. 상품 조회
        List<Product> products = productRepository.findAll();

        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        // 2. 옵션 조회 (구매 가능한 옵션만)
        List<ProductOption> options = productOptionRepository.findByProductIdIn(
                productIds
        );

        // 3. 옵션을 상품 ID 기준으로 그룹화
        Map<Long, List<ProductOption>> optionsByProduct = options.stream()
                .collect(Collectors.groupingBy(po -> po.getProduct().getId()));

        // 4. 각 상품 DTO 생성
        List<ProductDetailResponse> result = products.stream()
                .map(product -> {
                    List<ProductOption> productOptions = optionsByProduct.getOrDefault(product.getId(), Collections.emptyList());

                    // 색상별 사이즈 그룹화
                    LinkedHashMap<ProductColor, List<Integer>> colorToSizes = productOptions.stream()
                            .collect(Collectors.groupingBy(
                                    ProductOption::getColor,
                                    LinkedHashMap::new,
                                    Collectors.mapping(ProductOption::getSize, Collectors.toList())
                            ));

                    List<ProductOptionResponse> optionDtos = colorToSizes.entrySet().stream()
                            .map(e -> ProductOptionResponse.builder()
                                    .color(String.valueOf(e.getKey()))
                                    .sizes(e.getValue())
                                    .build())
                            .collect(Collectors.toList());

                    return ProductDetailResponse.builder()
                            .productId(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .category(product.getCategory().name())
                            .options(optionDtos)
                            .build();
                })
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 상품 생성 후 ID 반환
     * @param request
     * @param userId
     * @return
     */
    public Long ProductCreateAndReturnId(ProductCreateRequest request,String userId) {
        Product product = Product.createProduct(
                request.getName(),
                request.getPrice(),
                request.getDescription(),
                request.getCategory()
        );
        for (ProductOptionCreateRequest option : request.getOptions()) {
            ProductOption productOption = ProductOption.create(
                    product,
                    option.getColor(),
                    option.getSize(),
                    option.getStock()
            );
        }
        Product save = productRepository.save(product);
        return save.getId();
    }

    /**
     * 상품 상세 조회
     * @param productId
     * @return
     */
    @Transactional(readOnly = true)
    public ProductDetailResponse productDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        List<ProductOption> options = productOptionRepository.findByProductIdAndStatusAndStockGreaterThan(
                productId, OptionStatus.AVAILABLE, 0
        );

        // 색상별 사이즈 그룹화
        LinkedHashMap<ProductColor, List<Integer>> colorToSizes = options.stream()
                .collect(Collectors.groupingBy(
                        ProductOption::getColor,
                        LinkedHashMap::new,
                        Collectors.mapping(ProductOption::getSize, Collectors.toList())
                ));

        // DTO 변환
        List<ProductOptionResponse> optionDtos = colorToSizes.entrySet().stream()
                .map(e -> ProductOptionResponse.builder()
                        .color(String.valueOf(e.getKey()))
                        .sizes(e.getValue())
                        .build())
                .collect(Collectors.toList());

        return ProductDetailResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory().name())
                .options(optionDtos)
                .build();
    }

    public Long productMinusStock(ProductUpdateStockDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        ProductColor color = ProductColor.valueOf(dto.getColor().toUpperCase());

        ProductOption productOption = productOptionRepository.findByProductIdAndColorAndSize(product.getId(), color, dto.getSize());

        productOption.decreaseStock(dto.getStock());
        return product.getId();
    }


//
//    public Product productPlusStock(ProductUpdateStockDto dto) {
//        Product product = productRepository.findById(dto.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
//
//        product.plusStockQuantity(dto.getStockQuantity());
//        return product;
//    }
}
