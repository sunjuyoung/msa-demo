package sun.board.comment.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageCalculator {

    public static Long calculatePage(Long page, Long pageSize, Long moveablePageCount){
        return (((page -1)/ moveablePageCount) +1) * pageSize * moveablePageCount + 1;
    }
}
