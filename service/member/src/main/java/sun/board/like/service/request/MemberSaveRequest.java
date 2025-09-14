package sun.board.like.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import sun.board.like.entity.Member;

@NoArgsConstructor
@Data
public class MemberSaveRequest {
    private String name;
    private String email;
    private String password;

    public Member toEntity(String encodedPassword){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(encodedPassword)
                .build();
    }

}
