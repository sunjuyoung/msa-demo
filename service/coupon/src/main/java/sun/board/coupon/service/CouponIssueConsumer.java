package sun.board.coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.board.common.dataserializer.DataSerializer;
import sun.board.coupon.entity.Coupon;
import sun.board.coupon.entity.UserCoupon;
import sun.board.coupon.event.CouponIssueEvent;
import sun.board.coupon.exception.ex.AlreadyIssuedException;
import sun.board.coupon.exception.ex.NonRetryableKafkaException;
import sun.board.coupon.repository.CouponRepository;
import sun.board.coupon.repository.UserCouponRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueAppService couponIssueAppService;
    private final CouponRedisService couponRedisService;

    @KafkaListener(topics = "coupon-issue", groupId = "coupon-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void couponConsumer(String message) {

        CouponIssueEvent event;
        try {
            event = DataSerializer.deserialize(message, CouponIssueEvent.class);
        } catch (Exception e) {
            // 역직렬화 실패: 재시도 의미 없음 → DLT로 보냄(throw)
            throw new NonRetryableKafkaException(e.getMessage());
        }

        Long couponId = event.getCouponId();
        Long userId   = event.getUserId();


        try {
            Coupon coupon = couponIssueAppService.getCouponOrThrow(couponId);

            //중복 발급 체크
            couponIssueAppService.isAlreadyIssued(couponId, userId);

            //db insert
            couponIssueAppService.issueToUser(userId, coupon);

        }catch (AlreadyIssuedException e){

            log.info("coupon-issue-consumer already issued");
             couponRedisService.decrIssuedCount(couponId);
        }catch (DataIntegrityViolationException e){
            //동시성 문제로 인해 중복 발급된 경우
            log.info("coupon-issue-consumer already issued by concurrency");
            couponRedisService.decrIssuedCount(couponId);
        }catch (Exception e){
            //예외 발생 시 ack 생략 → 재처리 or DLQ로 이동
            log.error("CouponIssueConsumer 처리 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }





    }
}