package hongikchildren.carefriends.infra.videocall.service;

import hongikchildren.carefriends.infra.fcm.dto.FcmSendDto;
import hongikchildren.carefriends.infra.fcm.FcmServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoCallService {

    private final FcmServiceImpl fcmServiceImpl;

    @Transactional
    public void requestVideoCall(UUID friendId, String roomName) {
        FcmSendDto fcmSendDto = FcmSendDto.builder()
                .id(friendId)
                .title("영상 통화 요청을 확인하세요")
                .body("보호자로부터 영상 통화 요청이 왔습니다.")
                .receiverType("Friend")
                .type("call")
                .data(roomName)
                .build();

        try {
            fcmServiceImpl.sendMessageTo(fcmSendDto);
            System.out.println("FCM 메시지 전송 성공: " + fcmSendDto.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FCM 메시지 전송 실패");
            throw new RuntimeException("FCM 메시지 전송에 실패했습니다.");
        }
    }
}
