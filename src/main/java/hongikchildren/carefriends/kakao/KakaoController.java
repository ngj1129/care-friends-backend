package hongikchildren.carefriends.kakao;

import hongikchildren.carefriends.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getcode(@RequestParam("code") String code) {

        System.out.println("카카오 코드: " + code);
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        System.out.println("카카오 토큰: " + accessToken);

        //이메일로 이미 등록된 회원인지 확인해서 새로운 회원이면 회원가입 화면 보여줌
        //추가로 등록할 정보: 이름, 전화번호, 성별, 생년월일
        boolean isExistingUser = userService.isExistingUser(userInfo.getKakaoAccount().getEmail());

        if (isExistingUser) {
            // 기존 회원 처리 로직
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            // 새로운 회원이면 리액트 네이티브 앱으로 리다이렉트
            String redirectUrl = "myapp://signup?email=" + userInfo.getKakaoAccount().getEmail();
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
        }
    }
}
