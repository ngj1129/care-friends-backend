package hongikchildren.carefriends.infra.redis;

import hongikchildren.carefriends.caregiver.dto.response.FriendInfoResponse;
import hongikchildren.carefriends.infra.auth.security.CustomUserDetails;
import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.task.service.TaskService;
import hongikchildren.carefriends.user.domain.User;
import hongikchildren.carefriends.user.dto.response.ProfileResponse;
import hongikchildren.carefriends.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

//    @GetMapping("/getFriends")
//    public List<FriendInfoResponse> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        String email = userDetails.getUsername();
//        System.out.println("JWT에서 추출된 이메일: " + email);
//
//        if (!(userDetails.getUser() instanceof Caregiver)) {
//            throw new IllegalStateException("사용자가 Caregiver가 아닙니다.");
//        }
//        Caregiver caregiver = (Caregiver) userDetails.getUser();
//
//        return caregiver.getFriends().stream()
//                .map(friend -> new FriendInfoResponse(
//                        friend.getId(),
//                        friend.getName(),
//                        friend.getPhoneNumber(),
//                        friend.getBirthDate(),
//                        friend.getGender(),
//                        friend.getProfileImg()
//                ))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/profile/redis")
    public ProfileResponse getProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getEmail();
        System.out.println("JWT에서 추출된 이메일: " + email);

        User user = userService.getUserByEmail(email);
        return new ProfileResponse(user.getProfileImg(), user.getId(), user.getName(), user.getPhoneNumber(), user.getBirthDate(), user.getGender());
    }

    @GetMapping("/redis")
    public String getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getId() + " " + userDetails.getEmail();
    }

    @GetMapping("/profile")
    public ProfileResponse getProfileInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        User user = userService.getUserByEmail(email);
        return new ProfileResponse(user.getProfileImg(), user.getId(), user.getName(), user.getPhoneNumber(), user.getBirthDate(), user.getGender());
    }

}
