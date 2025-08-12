package sun.board.member.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import sun.board.member.entity.Member;

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
