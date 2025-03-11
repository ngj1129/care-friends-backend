package hongikchildren.carefriends.task.api.caregiver;

import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.dto.NewTaskResponse;
import hongikchildren.carefriends.task.dto.PerTaskResponse;
import hongikchildren.carefriends.task.dto.caregiver.CaregiverNewTaskRequest;
import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class CaregiverTaskController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    /**
     * 일정 추가
     */
    @PostMapping("/caregiver")
    public ResponseEntity<NewTaskResponse> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CaregiverNewTaskRequest request) {
        // JWT에서 이메일 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtUtil.getEmail(token); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 이메일로 보호자 정보 조회
        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow(() -> new IllegalArgumentException("보호자를 찾을 수 없습니다."));

        // id로 친구 정보 조회
        Friend friend = friendService.getFriendById(request.getFriendId()).orElseThrow();

        // Task 생성 및 저장
        Task task = taskService.saveTask(
                friend,
                request.getDate(),
                request.getStartTime(),
                request.getTitle(),
                request.getLocation(),
                request.getMemo(),
                request.getPeriodType(),
                request.getPeriod()
        );

        return new ResponseEntity<>(new NewTaskResponse(task.getGroupId()), HttpStatus.OK);
    }

    // 보호자의 프렌즈로 등록된 노약자의 일정 불러오기 (특정e 날짜 일정 혹은 전체 일정)
    @GetMapping("/{friendId}")
    public List<PerTaskResponse> getMyFriendTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID friendId,
            @RequestParam(required = false) String date) {

        // JWT에서 이메일 추출
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 이메일로 보호자 정보 조회
        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

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
                task -> PerTaskResponse.builder()
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
}
