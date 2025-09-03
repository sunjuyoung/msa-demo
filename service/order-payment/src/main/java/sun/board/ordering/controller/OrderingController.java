package sun.board.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.board.ordering.dto.ApiResponse;
import sun.board.ordering.dto.OrderCreateDto;
import sun.board.ordering.dto.OrderHistoryResponse;
import sun.board.ordering.dto.OrderViewResponseDTO;
import sun.board.ordering.service.OrderingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordering")
public class OrderingController {

    private final OrderingService orderingService;

    //주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrderViewResponseDTO> orderCreate(@RequestBody OrderCreateDto dto) {
        OrderViewResponseDTO orderViewResponseDTO = orderingService.orderCreateFeign(dto);
        return ResponseEntity.ok().body(orderViewResponseDTO);
    }

    //주문내역 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        OrderHistoryResponse orders = orderingService.getOrdersByUser(userId);
        return ResponseEntity.ok().body(ApiResponse.success(orders));
    }

    //상태 cancel 변경
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long orderId) {
        orderingService.cancelOrder(orderId);
        return ResponseEntity.ok().body(ApiResponse.success("Order cancelled successfully"));
    }



}
