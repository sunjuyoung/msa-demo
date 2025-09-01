package sun.board.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.dto.request.ProductCreateRequest;
import sun.board.product.dto.request.ProductOptionCreateRequest;
import sun.board.product.dto.request.ProductSearchRequest;
import sun.board.product.dto.response.*;
import sun.board.product.dto.response.list.PageResult;
import sun.board.product.dto.response.list.ProductListItemResponse;
import sun.board.product.dto.response.list.ProductOptionRow;
import sun.board.product.dto.response.list.ProductRow;
import sun.board.product.dto.response.listv2.ProductListQuery;
import sun.board.product.dto.response.listv2.ProductRowV2;
import sun.board.product.entity.Product;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;
import sun.board.product.mapper.ProductMapper;
import sun.board.product.repository.ProductOptionRepository;
import sun.board.product.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductMapper productMapper;


    // 간단한 페이지 응답용 DTO (원하시면 record로 바꿔도 됩니다)
    public static class PageResultV2<T> {
        private final List<T> content;
        private final int totalCount;

        public PageResultV2(List<T> content, int totalCount) {
            this.content = content;
            this.totalCount = totalCount;
        }

        public List<T> getContent() { return content; }
        public int getTotalCount() { return totalCount; }
    }


    public PageResultV2<ProductRowV2> getProductPage(ProductListQuery query) {
        List<ProductRowV2> rows = productMapper.findProductPageOneShot(query);

        int total = 0;
        if (!rows.isEmpty() && rows.get(0).getTotalCount() != null) {
            total = rows.get(0).getTotalCount(); // 한방쿼리에서 모든 행에 동일 total_count 부여
        }

        return new PageResultV2<>(rows, total);
    }

    /**
     * 상품 검색 페이지
     * @param searchRequest
     * @return
     */
    @Transactional(readOnly = true)
    public PageResult<ProductListItemResponse> getProductList(ProductSearchRequest searchRequest) {
        // 전체 개수 조회
        int offset = Math.max(0, (searchRequest.getPage() - 1)) * searchRequest.getSize();
        List<String> normColors = null;
        if (searchRequest.getColors() != null) {
            normColors = searchRequest.getColors().stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.toUpperCase(Locale.ROOT))
                    .toList();
        }

        searchRequest.setOffset(offset);
        // 1) 총 상품 수
        int total = productMapper.countProducts(
                searchRequest.getName(),
                searchRequest.getCategory() != null ? searchRequest.getCategory() : null,
                searchRequest.getMinPrice() != null ? searchRequest.getMinPrice().intValue() : null,
                searchRequest.getMaxPrice() != null ? searchRequest.getMaxPrice().intValue() : null,
                searchRequest.getSizes() != null ? searchRequest.getSizes() : null,
                normColors != null ? normColors : null
        );
        if (total == 0) {
            return new PageResult<>(List.of(), 0, searchRequest.getPage(), searchRequest.getSize());
        }

        // 2) 현재 페이지의 상품 id 목록
        List<Long> pageIds = productMapper.findProductPageIds(
                offset, searchRequest.getSize(),
                searchRequest.getName(),
                searchRequest.getCategory() != null ? searchRequest.getCategory() : null,
                searchRequest.getMinPrice() != null ? searchRequest.getMinPrice().intValue() : null,
                searchRequest.getMaxPrice() != null ? searchRequest.getMaxPrice().intValue() : null,
                searchRequest.getSizes() != null ? searchRequest.getSizes() : null,
                normColors != null ? normColors : null

        );

        if (pageIds.isEmpty()) {
            return new PageResult<>(List.of(), total, searchRequest.getPage(), searchRequest.getSize());
        }

        // 3) 상품/옵션 일괄 조회
        List<ProductRow> products = productMapper.findProductsByIds(pageIds);
        List<ProductOptionRow> options = productMapper.findOptionsByProductIds(pageIds);

        // 4) productId -> 옵션 목록
        Map<Long, List<ProductOptionRow>> optionsByProduct = options.stream()
                .collect(Collectors.groupingBy(ProductOptionRow::getProductId, LinkedHashMap::new, Collectors.toList()));

        // 5) 최종 응답 조립 (중복 제거 + 정렬)
        List<ProductListItemResponse> content = new ArrayList<>(products.size());
        for (ProductRow p : products) {
            List<ProductOptionRow> pos = optionsByProduct.getOrDefault(p.getProductId(), List.of());

            // size: 정수 오름차순 / color: 소문자 알파벳 정렬
            List<Integer> sizes = pos.stream()
                    .map(ProductOptionRow::getSize)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .toList();

            List<String> colors = pos.stream()
                    .map(ProductOptionRow::getColor)
                    .filter(Objects::nonNull)
                    .map(String::toLowerCase)
                    .distinct()
                    .sorted()
                    .toList();

            content.add(ProductListItemResponse.builder()
                    .productId(p.getProductId())
                    .name(p.getName())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .category(p.getCategory())
                    .size(sizes)
                    .color(colors)
                    .build());
        }

        // 6) 페이지 결과 반환
        return new PageResult<>(content, total, searchRequest.getPage(), searchRequest.getSize());
    }



    /**
     * 상품 리스트
     */
    public List<ProductResponse> getProducts() {

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
        List<ProductResponse> result = products.stream()
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

                    return ProductResponse.builder()
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

        Map<String, List<OptionSizeWithStockDto>> colorMap = options.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getColor().name(),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                o -> new OptionSizeWithStockDto(o.getSize(), o.getStock()),
                                Collectors.toList()
                        )
                ));

        // DTO 변환
        List<ProductOptionWithStockDto> optionDtos = colorMap.entrySet().stream()
                .map(e -> ProductOptionWithStockDto.builder()
                        .color(e.getKey())
                        .sizes(e.getValue().stream()
                                .sorted(Comparator.comparing(OptionSizeWithStockDto::getSize))
                                .collect(Collectors.toList()))
                        .build())
                .sorted(Comparator.comparing(ProductOptionWithStockDto::getColor))
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
