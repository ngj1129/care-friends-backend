package hongikchildren.carefriends.jwt;

import hongikchildren.carefriends.kakao.KakaoService;
import hongikchildren.carefriends.kakao.KakaoUserInfoResponseDto;
import hongikchildren.carefriends.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JWTController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> getjwt(@RequestBody Map<String, String> requestBody) {

        String accessToken = requestBody.get("accessToken");
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        System.out.println("카카오 토큰: " + accessToken);

        //이메일로 이미 등록된 회원인지 확인
        String isExistingUser = userService.isExistingUser(userInfo.getKakaoAccount().getEmail());

        if (!isExistingUser.equals("null")) {
            // 이미 등록된 회원이라면 JWT 토큰을 생성하고 SecurityContext에 인증 정보를 저장
            String email = userInfo.getKakaoAccount().getEmail();
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, null);

            // 인증 객체를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // JWT 토큰 발급
            String jwtToken = jwtUtil.createJwt(email);
            System.out.println("JWT 토큰: " + jwtToken);

            // 응답에 JWT 토큰과 유저 타입 포함
            Map<String, String> response = new HashMap<>();
            response.put("jwtToken", jwtToken);
            response.put("userType", isExistingUser);

            return ResponseEntity.ok(response);


        }

        else {
            // 새로운 회원임을 명시하는 응답
            Map<String, String> response = new HashMap<>();
            response.put("status", "newUser");
            response.put("email", userInfo.getKakaoAccount().getEmail());

            return ResponseEntity.ok(response);
        }
    }
}