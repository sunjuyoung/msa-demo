package sun.board.hotproduct.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculatorUtils {

    public static Duration calculateDurationToMidnight(){
        LocalDateTime now = LocalDateTime.now();
        //현재 날짜·시간에서 하루 더한 값을 만들고
        //시간 부분을 00:00(자정) 으로 치환합니다.
        LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIDNIGHT);

        //현재 시간과 자정의 차이를 계산하여 반환합니다.
        return Duration.between(now,midnight);

    }
}


