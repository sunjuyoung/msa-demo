package sun.board.article.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageCalculatorTest {

    @Test
    void calcualgePageTest(){

        calculatePageLimiTest(1L, 30L, 10L, 301L);
        calculatePageLimiTest(7L, 30L, 10L, 301L);
        calculatePageLimiTest(10L, 30L, 10L, 301L);
        calculatePageLimiTest(11L, 30L, 10L, 601L);
    }

    void calculatePageLimiTest(Long page, Long pageSize, Long moveablePageCount, Long expected){
        Long result = PageCalculator.calculatePage(page, pageSize, moveablePageCount);
        assertEquals(expected, result);

    }

}