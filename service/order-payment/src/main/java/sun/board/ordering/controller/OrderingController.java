package sun.board.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sun.board.ordering.dto.ApiResponse;
import sun.board.ordering.dto.OrderCreateDto;
import sun.board.ordering.service.OrderingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordering")
public class OrderingController {

    private final OrderingService orderingService;

    @PostMapping("/create")
    public ApiResponse orderCreate(@RequestBody OrderCreateDto dto,  @RequestHeader("X-User-Id") String userId) {
        Long result = orderingService.orderCreate(dto, userId);
        return ApiResponse.success(result);
    }
}
