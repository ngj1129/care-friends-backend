package hongikchildren.carefriends.friend.api;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.dto.response.CaregiverInfoResponse;
import hongikchildren.carefriends.friend.repository.FriendRepository;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.friendrequest.service.FriendRequestService;
import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendRequest")
public class FriendGetCaregiverController {

    private final FriendRequestService friendRequestService;
    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;
    private final JWTUtil jwtUtil;
    private final CaregiverService caregiverService;
    private final FriendService friendService;

    // 프렌즈의 보호자 정보 조회
    @GetMapping("/getCaregiver")
    public CaregiverInfoResponse getCaregiver(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);
        Friend friend =  friendRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌즈 찾을 수 없음"));

        Caregiver caregiver = friend.getCaregiver();
        if (caregiver == null) {
            return null; // 보호자가 없을 경우 null 반환
        }

        return new CaregiverInfoResponse(
                caregiver.getName(),
                caregiver.getPhoneNumber(),
                caregiver.getBirthDate(),
                caregiver.getGender(),
                caregiver.getProfileImg()
        );
    }
}
