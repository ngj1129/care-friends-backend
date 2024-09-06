package hongikchildren.carefriends.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId); // 카카오 클라이언트 ID
        params.add("code", code);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 카카오 서버에 POST 요청 보내기
        ResponseEntity<KakaoTokenResponseDto> responseEntity = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponseDto.class
        );

        // 응답에서 액세스 토큰 추출
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            KakaoTokenResponseDto responseBody = responseEntity.getBody();
            if (responseBody != null) {
                return responseBody.getAccessToken();
            }
        }

        throw new RuntimeException("Failed to retrieve access token from Kakao");
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        String url = KAUTH_USER_URL_HOST + "/v2/user/me";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KakaoUserInfoResponseDto.class
            );

            KakaoUserInfoResponseDto userInfo = response.getBody();

            log.info("[ Kakao Service ] Auth ID ---> {}", userInfo.getId());
            log.info("[ Kakao Service ] NickName ---> {}", userInfo.getKakaoAccount().getProfile().getNickName());
            log.info("[ Kakao Service ] ProfileImageUrl ---> {}", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

            return userInfo;

        } catch (HttpClientErrorException e) {
            log.error("4xx Error while fetching user info: {}", e.getMessage());
            throw new RuntimeException("Invalid Parameter");
        } catch (HttpServerErrorException e) {
            log.error("5xx Error while fetching user info: {}", e.getMessage());
            throw new RuntimeException("Internal Server Error");
        } catch (Exception e) {
            log.error("Error while fetching user info: {}", e.getMessage());
            throw new RuntimeException("An error occurred while fetching user info");
        }
    }
}

