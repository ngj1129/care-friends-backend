package hongikchildren.carefriends.kakao;

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

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getcode(@RequestParam("code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
