package sun.board.product.grpc;

import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import sun.board.product.entity.Product;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;
import sun.board.product.repository.ProductOptionRepository;
import sun.board.product.repository.ProductRepository;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService  extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<GetProductResponse> responseObserver) {

        long productId = request.getProductId();
        String color = request.getColor();
        long size = request.getSize();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        ProductColor productColor = ProductColor.valueOf(color);

        ProductOption productOption =
                productOptionRepository.findByProductIdAndColorAndSize(productId, productColor, (int) size);


        sun.board.product.grpc.Product productGrpc = sun.board.product.grpc.Product.newBuilder()
                .setId(productOption.getId())
                .setName(product.getName())
                .setCategory(product.getCategory().name())
                .setPrice(product.getPrice().intValue())
                .setColor(productOption.getColor().name())
                .setSize(productOption.getSize())
                .setStock(productOption.getStock())
                .build();

        GetProductResponse response = GetProductResponse.newBuilder()
                .setProduct(productGrpc)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
