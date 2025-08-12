package sun.board.hotarticle.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TimeCalculatorUtilsTest {


    @Test
    void calculateDurationToMidnight() {
        Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();

        System.out.println("min : " + duration.toMinutes());
        System.out.println("sec/60 : " + duration.getSeconds()/60);
    }
}