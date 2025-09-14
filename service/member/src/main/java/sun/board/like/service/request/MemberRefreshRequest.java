package sun.board.like.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberRefreshRequest {

    private String refreshToken;
}
