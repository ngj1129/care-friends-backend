package hongikchildren.carefriends.redis;

import com.amazonaws.services.kms.model.NotFoundException;
import hongikchildren.carefriends.api.FriendApiController;
import hongikchildren.carefriends.api.FriendTaskApiController;
import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.jwt.CustomUserDetails;
import hongikchildren.carefriends.jwt.JWTUtil;
import hongikchildren.carefriends.redis.dto.CaregiverDTO;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.TaskService;
import lombok.Builder;
import lombok.Data;
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
@RequestMapping("/redis/test")
public class RedisTestController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    // 보호자의 프렌즈로 등록된 노약자의 일정 불러오기 (특정e 날짜 일정 혹은 전체 일정)
    @GetMapping("/{friendId}")
    public List<perTaskResponse> getMyFriendTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID friendId,
            @RequestParam(required = false) String date) {

        // JWT에서 이메일 추출
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Caregiver caregiver = (Caregiver) userDetails.getUser();

        // 이메일로 보호자 정보 조회
//        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
//                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

        // 해당 보호자의 친구 중 요청한 friendId와 일치하는 친구의 일정만 가져오기
        Friend friend = caregiver.getFriends().stream()
                .filter(f -> f.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 친구를 찾을 수 없습니다."));


        List<Task> tasks;
        if(date != null){
            LocalDate requestedDate = LocalDate.parse(date);
            tasks = taskService.getTasksByFriendAndDate(friend.getId(), requestedDate);
        } else {
            tasks = taskService.getTasksByFriend(friend.getId());
        }


        // 일정 정보를 반환
        return tasks.stream().map(
                task -> perTaskResponse.builder()
                        .id(task.getId())
                        .location(task.getLocation())
                        .memo(task.getMemo())
                        .signalTime(task.getSignalTime())
                        .startTime(task.getStartTime())
                        .status(task.getStatus())
                        .taskType(task.getTaskType())
                        .title(task.getTitle())
                        .date(task.getDate())
                        .build()
        ).collect(Collectors.toList());
    }

//    @GetMapping("/getFriends")
//    public List<FriendApiController.FriendInfoResponse> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        String email = userDetails.getUsername();
//        System.out.println("JWT에서 추출된 이메일: " + email);
//
//        if (!(userDetails.getUser() instanceof Caregiver)) {
//            throw new IllegalStateException("사용자가 Caregiver가 아닙니다.");
//        }
//        Caregiver caregiver = (Caregiver) userDetails.getUser();
//
//        return caregiver.getFriends().stream()
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

    @GetMapping("/getFriends")
    public List<FriendApiController.FriendInfoResponse> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("🔍 JWT에서 추출된 이메일: " + email);

        if (!(userDetails.getUser() instanceof CaregiverDTO)) {
            throw new IllegalStateException("사용자가 Caregiver가 아닙니다.");
        }
        CaregiverDTO caregiver = (CaregiverDTO) userDetails.getUser();

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



    @Data
    @Builder
    static class perTaskResponse {
        private Long id;
        private String memo;
        private LocalTime startTime;
        private LocalTime signalTime;
        private String location;
        private String title;
        private Status status;
        private TaskType taskType;
        private LocalDate date;

        @Builder
        public perTaskResponse(Long id, String memo, LocalTime startTime, LocalTime signalTime, String location, String title, Status status, TaskType taskType, LocalDate date) {
            this.id = id;
            this.memo = memo;
            this.startTime = startTime;
            this.signalTime = signalTime;
            this.location = location;
            this.title = title;
            this.status = status;
            this.taskType = taskType;
            this.date = date;
        }
    }


}
