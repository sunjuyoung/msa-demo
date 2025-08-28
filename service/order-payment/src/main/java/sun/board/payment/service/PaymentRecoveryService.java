package sun.board.payment.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import sun.board.payment.entity.PaymentEvent;
import sun.board.payment.repository.PaymentEventRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentRecoveryService {

    private final PaymentEventRepository paymentEventRepository;


   //@Scheduled(fixedDelay = 180, timeUnit = TimeUnit.SECONDS)
    public void recovery(){

        //완료 되지 않은 결제건을 조회, 3분 전 결제건

        Optional<PaymentEvent> paymentEvent =
        paymentEventRepository.getPendingPaymentEvent(LocalDateTime.now());


    }

    
}
