package hongikchildren.carefriends.redis;

import hongikchildren.carefriends.api.FriendApiController;
import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.jwt.CustomUserDetails;
import hongikchildren.carefriends.jwt.JWTUtil;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
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
    public List<FriendApiController.FriendInfoResponse> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        if (!(userDetails.getUser() instanceof Caregiver)) {
            throw new IllegalStateException("사용자가 Caregiver가 아닙니다.");
        }
        Caregiver caregiver = (Caregiver) userDetails.getUser();

        return caregiver.getFriends().stream()
                .map(friend -> new FriendApiController.FriendInfoResponse(
                        friend.getId(),
                        friend.getName(),
                        friend.getPhoneNumber(),
                        friend.getBirthDate(),
                        friend.getGender(),
                        friend.getProfileImg()
                ))
                .collect(Collectors.toList());
    }

    // 보호자가 관리하는 모든 프렌즈 Id 조회 - 기존 컨트롤러
//    @GetMapping("/getFriends")
//    public List<FriendApiController.FriendInfoResponse> getFriends(@AuthenticationPrincipal UserDetails userDetails){
//        String email = userDetails.getUsername(); // JWT에서 이메일 추출
//        System.out.println("JWT에서 추출된 이메일: " + email);
//
//        return caregiverService.getCaregiverByEmail(email)
//                .orElseThrow(() -> new NotFoundException("caregiver not found"))
//                .getFriends().stream()
//                .map(friend -> new FriendApiController.FriendInfoResponse(
//                        friend.getId(),
//                        friend.getName(),
//                        friend.getPhoneNumber(),
//                        friend.getBirthDate(),
//                        friend.getGender(),
//                        friend.getProfileImg()
//                ))
//                .collect(Collectors.toList());
//    }


}
