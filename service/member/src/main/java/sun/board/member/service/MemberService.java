package sun.board.member.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.member.entity.Member;
import sun.board.member.repository.MemberRepository;
import sun.board.member.service.request.LoginRequest;
import sun.board.member.service.request.MemberSaveRequest;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long save(MemberSaveRequest memberSaveRequest){
        Optional<Member> optionalMember =  memberRepository.findByEmail(memberSaveRequest.getEmail());
        if(optionalMember.isPresent()){
            throw new IllegalArgumentException("기존에 존재하는 회원입니다.");
        }
        String password = passwordEncoder.encode(memberSaveRequest.getPassword());
        Member member = memberRepository.save(memberSaveRequest.toEntity(password));
        return  member.getId();
    }
    public Member login(LoginRequest request){
        boolean check = true;
//        email존재여부
        Optional<Member> optionalMember = memberRepository.findByEmail(request.getEmail());
        if(!optionalMember.isPresent()){
            check = false;
        }
//        password일치 여부
        if(!passwordEncoder.matches(request.getPassword(), optionalMember.get().getPassword())){
            check =false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalMember.get();
    }
}