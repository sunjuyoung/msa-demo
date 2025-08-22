package sun.board.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sun.board.common.event.coupon.CouponIssueEvent;

@Service
@RequiredArgsConstructor
public class CouponIssueProducer {
    private final KafkaTemplate<String, CouponIssueEvent> kafkaTemplate;
    private static final String TOPIC = "coupon.issue";

    public void publish(CouponIssueEvent event) {
        // key를 couponId 또는 userId로 해시 분산
        kafkaTemplate.send(TOPIC, String.valueOf(event.getCouponId()), event);
    }
}