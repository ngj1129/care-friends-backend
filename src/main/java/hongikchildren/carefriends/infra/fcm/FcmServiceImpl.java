package hongikchildren.carefriends.infra.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import hongikchildren.carefriends.infra.fcm.dto.FcmMessageDto;
import hongikchildren.carefriends.infra.fcm.dto.FcmSendDto;
import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
import hongikchildren.carefriends.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private final FriendRepository friendRepository;
    private final CaregiverRepository caregiverRepository;

    // 메시지 전송
    @Override
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {

        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/carefriends-121e6/messages:send";
        ResponseEntity response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        System.out.println(response.getStatusCode());

        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
    }

    // 토큰 발급
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/carefriends-121e6-firebase-adminsdk-v1geu-f14d7de41a.json";


        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // 메시지 구성
    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        String fcmToken;
        if ("Friend".equals(fcmSendDto.getReceiverType())) {
            fcmToken = friendRepository.findById(fcmSendDto.getId())
                    .orElseThrow(() -> new RuntimeException("프렌드 찾을 수 없음"))
                    .getFcmToken();
        } else if ("Caregiver".equals(fcmSendDto.getReceiverType())) {
            fcmToken = caregiverRepository.findById(fcmSendDto.getId())
                    .orElseThrow(() -> new RuntimeException("보호자 찾을 수 없음"))
                    .getFcmToken();
        } else {
            throw new RuntimeException("Invalid receiver type");
        }

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("type", fcmSendDto.getType());
        dataMap.put("roomName", fcmSendDto.getData()); // `data`에 roomName을 포함

        // FCM 메시지 생성
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmToken)
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        )
                        .data(dataMap)
                        .build()
                )
                .validateOnly(false)
                .build();

        return om.writeValueAsString(fcmMessageDto);
    }
}
