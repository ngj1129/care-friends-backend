package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Medicine;
import hongikchildren.carefriends.domain.TakeTime;
import hongikchildren.carefriends.s3.S3Uploader;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendRequestService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FriendApiController {
    private final CaregiverService caregiverService;
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;

    // 친구 추가 요청
    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestParam UUID caregiverId, @RequestParam UUID friendUUID) {
        try {
            Caregiver caregiver = caregiverService.getCaregiverById(caregiverId)
                    .orElseThrow(() -> new RuntimeException("Caregiver not found"));

            friendRequestService.sendFriendRequest(caregiver, friendUUID);

            return ResponseEntity.ok("Friend request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

}
