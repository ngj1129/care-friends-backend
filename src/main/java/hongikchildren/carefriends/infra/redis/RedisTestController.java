package hongikchildren.carefriends.infra.redis;

import hongikchildren.carefriends.caregiver.dto.response.FriendInfoResponse;
import hongikchildren.carefriends.infra.auth.security.CustomUserDetails;
import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class RedisTestController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    @GetMapping("/getFriends")
    public List<FriendInfoResponse> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        if (!(userDetails.getUser() instanceof Caregiver)) {
            throw new IllegalStateException("사용자가 Caregiver가 아닙니다.");
        }
        Caregiver caregiver = (Caregiver) userDetails.getUser();

        return caregiver.getFriends().stream()
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

}
