package sun.board.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.product.dto.ProductRegisterDto;
import sun.board.product.dto.ProductResDto;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.entity.Product;
import sun.board.product.exception.ex.StockInsufficientException;
import sun.board.product.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public Product productCreate(ProductRegisterDto dto, String userId) {
        Product product = productRepository.save(dto.toEntity(Long.parseLong(userId)));
        return product;
    }

    @Transactional(readOnly = true)
    public ProductResDto productDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        return ProductResDto.from(product);
    }

    public Product productMinusStock(ProductUpdateStockDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        if (product.getStockQuantity() < dto.getStockQuantity()) {
            throw new StockInsufficientException("재고가 부족합니다.");
        }
        product.minusStockQuantity(dto.getStockQuantity());
        return product;
    }

    public Product productPlusStock(ProductUpdateStockDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        product.plusStockQuantity(dto.getStockQuantity());
        return product;
    }
}
