package hongikchildren.carefriends.caregiver.api;

import com.amazonaws.services.kms.model.NotFoundException;
import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.dto.response.FriendInfoResponse;
import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.repository.FriendRepository;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.friendrequest.service.FriendRequestService;
import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendRequest")
public class CaregiverGetFriendController {

    private final FriendRequestService friendRequestService;
    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;
    private final JWTUtil jwtUtil;
    private final CaregiverService caregiverService;
    private final FriendService friendService;

    // 보호자가 관리하는 모든 프렌즈 Id 조회
    @GetMapping("/getFriends")
    public List<FriendInfoResponse> getFriends(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername(); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        return caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new NotFoundException("caregiver not found"))
                .getFriends().stream()
                .map(friend -> new FriendInfoResponse(
                        friend.getId(),
                        friend.getName(),
                        friend.getPhoneNumber(),
                        friend.getBirthDate(),
                        friend.getGender(),
                        friend.getProfileImg()
                ))
                .collect(Collectors.toList());
    }

    // 보호자가 관리하는 친구 삭제 api
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriendFromCaregiver(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID friendId){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow();

        Friend friend = friendService.getFriendById(friendId).orElseThrow();

        caregiverService.deleteFriendFromCaregiver(caregiver.getId(), friend);
        return ResponseEntity.ok().build();
    }
}
