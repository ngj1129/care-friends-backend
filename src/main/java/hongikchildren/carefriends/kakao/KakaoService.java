package hongikchildren.carefriends.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@RequiredArgsConstructor
@Service
public class KakaoService {

    private final String clientId;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId) {
        this.clientId = clientId;
    }

    public String getAccessTokenFromKakao(String code) {
        String tokenUrl = KAUTH_TOKEN_URL_HOST + "/oauth/token";

        // 요청 파라미터 설정
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", clientId);
        params.put("code", code);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 바디 및 헤더 설정
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // RestTemplate을 사용하여 POST 요청
        ResponseEntity<KakaoTokenResponseDto> responseEntity = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponseDto.class
        );

        KakaoTokenResponseDto kakaoTokenResponseDto = responseEntity.getBody();

        if (kakaoTokenResponseDto == null) {
            throw new RuntimeException("Failed to retrieve access token from Kakao");
        }

        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }
}

