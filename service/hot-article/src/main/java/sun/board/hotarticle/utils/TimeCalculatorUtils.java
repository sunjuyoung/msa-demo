package sun.board.hotarticle.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculatorUtils {

    //ttl 계산
    //현재 시간부터 자정까지 얼마 남았는지
    public static Duration calculateDurationToMidnight(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIDNIGHT);//다음날에 자정 00시 00분 00초
        return Duration.between(now, midnight);

    }
}
