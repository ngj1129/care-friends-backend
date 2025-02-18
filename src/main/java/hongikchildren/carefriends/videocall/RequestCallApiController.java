package hongikchildren.carefriends.videocall;

import hongikchildren.carefriends.api.CaregiverTaskApiController;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videocall")
public class RequestCallApiController {

    private final VideoCallService videoCallService;

    @PostMapping
    public ResponseEntity<String> requestVideoCall(@RequestBody VideoCallRequest videoCallRequest) {
        videoCallService.requestVideoCall(videoCallRequest.friendId, videoCallRequest.roomName);
        return ResponseEntity.ok("알림이 전송되었습니다.");

    }

    @Data
    static class VideoCallRequest {
        private UUID friendId;
        private String roomName;

        public VideoCallRequest(UUID friendId, String roomName) {
            this.friendId = friendId;
            this.roomName = roomName;
        }
    }
}
