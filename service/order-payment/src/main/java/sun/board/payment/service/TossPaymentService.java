package sun.board.payment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import sun.board.payment.dto.Failure;
import sun.board.payment.dto.PaymentExtraDetails;
import sun.board.payment.dto.request.PaymentConfirmCommand;
import sun.board.payment.dto.request.PaymentExecutionResult;
import sun.board.payment.dto.response.TossPaymentConfirmResponse;
import sun.board.payment.entity.enums.PSPConfirmStatus;
import sun.board.payment.entity.enums.PaymentMethod;
import sun.board.payment.entity.enums.PaymentType;
import sun.board.payment.entity.enums.TossPaymentError;
import sun.board.payment.exception.ex.PSPConfirmationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final WebClient tossWebClient;
    private static String uri = "/v1/payments/confirm";

    public Mono<PaymentExecutionResult> execute(PaymentConfirmCommand command){
        log.info("----------TossPaymentService.execute====================");


        return tossWebClient.post()
                .uri(uri)
                .header("Idempotency-Key", command.getOrderId())
                .bodyValue(
                        """
{
                            "paymentKey": "%s",
                            "orderId": "%s",
                            "amount": "%s"
                        }
                        """.formatted(command.getPaymentKey(), command.getOrderId(), command.getAmount())

                )
                .retrieve()
                .onStatus(statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(), response ->
                        response.bodyToMono(Failure.class)
                                .flatMap(failure -> {
                                    TossPaymentError error = TossPaymentError.get(failure.getCode());
                                    return Mono.error(new PSPConfirmationException( error.getMessage(),error.isRetryableError()));
                                })
                )
                .bodyToMono(TossPaymentConfirmResponse.class)
                .map(res->{
                    PaymentExtraDetails extraDetails = PaymentExtraDetails.builder()
                            .type(PaymentType.valueOf(res.getType()))
                            .method(PaymentMethod.getPaymentMethod(res.getMethod()))
                            .approvedAt(LocalDateTime.parse(res.getApprovedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                            .orderName(res.getOrderName())
                            .pspConfirmStatus(PSPConfirmStatus.valueOf(res.getStatus()))
                            .totalAmount((long) res.getTotalAmount())
                            .pspRawData(res.toString())
                            .build();
                    return PaymentExecutionResult.builder()
                            .extraDetails(extraDetails)
                            .paymentKey(command.getPaymentKey())
                            .orderId(command.getOrderId())
                            .isSuccess(true)
                            .build();
                    //재시도 2회
                }).retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .jitter(0.5)
                        .filter(throwable -> throwable instanceof PSPConfirmationException &&((PSPConfirmationException) throwable).isRetryableError())
                        .doBeforeRetry(retrySignal -> log.info("retry count : {}", retrySignal.totalRetries()))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->

                                // 재시도 후에도 실패하면 customException아닌 그대로 에러를 던진다.
                                retrySignal.failure()
                        )
                )
                ;
    }
}
