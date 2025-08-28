package sun.board.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.board.ordering.dto.ApiResponse;
import sun.board.ordering.dto.OrderCreateDto;
import sun.board.ordering.dto.OrderViewResponseDTO;
import sun.board.ordering.service.OrderingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordering")
public class OrderingController {

    private final OrderingService orderingService;

    @PostMapping("/create")
    public ResponseEntity<OrderViewResponseDTO> orderCreate(@RequestBody OrderCreateDto dto) {
        OrderViewResponseDTO orderViewResponseDTO = orderingService.orderCreateFeign(dto);
        return ResponseEntity.ok().body(orderViewResponseDTO);
    }
}
