package sun.board.ordering.entity;

public enum OrderStatus {
    PENDING, // 주문 생성됨, 재고 감소 대기 중
    COMPLETED, // 주문 완료 (재고 감소 성공)
    CANCELLED // 주문 취소 (재고 감소 실패)
}