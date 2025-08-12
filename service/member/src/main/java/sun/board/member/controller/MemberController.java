package sun.board.member.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.board.member.entity.Member;
import sun.board.member.service.JwtTokenProvider;
import sun.board.member.service.MemberService;
import sun.board.member.service.request.LoginRequest;
import sun.board.member.service.request.MemberRefreshRequest;
import sun.board.member.service.request.MemberSaveRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

  //  @Qualifier("rtdb")
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;



    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveRequest request){
        Long memberId = memberService.save(request);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest request){


//        email, password 검증
        Member member = memberService.login(request);
//        토큰 생성 및 return
        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());
//        redis에 rt저장
        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, TimeUnit.DAYS);//200일 ttl
//        사용자에게 at, rt지급
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", token);
        loginInfo.put("refreshToken", refreshToken);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAt(@RequestBody MemberRefreshRequest request){
//        rt디코딩 후 email추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(request.getRefreshToken())
                .getBody();
//        rt를 redis의 rt비교 검증
        Object rt =  redisTemplate.opsForValue().get(claims.getSubject());
        if(rt ==null || !rt.toString().equals(request.getRefreshToken())){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
//        at생성하여 지급
        String token = jwtTokenProvider.createToken(claims.getSubject(), claims.get("role").toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("token", token);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

}
