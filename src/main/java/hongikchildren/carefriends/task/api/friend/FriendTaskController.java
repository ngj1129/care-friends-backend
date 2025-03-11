package hongikchildren.carefriends.task.api.friend;

import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.dto.NewTaskResponse;
import hongikchildren.carefriends.task.dto.PerTaskResponse;
import hongikchildren.carefriends.task.dto.friend.FriendNewTaskRequest;
import hongikchildren.carefriends.task.service.TaskService;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class FriendTaskController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    /**
     * 일정 추가
     */
    @PostMapping
    public ResponseEntity<NewTaskResponse> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody FriendNewTaskRequest request) {
        // JWT에서 이메일 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtUtil.getEmail(token); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 이메일로 친구 정보 조회
        Friend friend = friendService.getFriendByEmail(email).orElseThrow(() -> new IllegalArgumentException("친구를 찾을 수 없습니다."));

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

    /*
    일정 조회
     */
    @GetMapping("/myTask")
    public List<PerTaskResponse> getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String date) {

        // JWT에서 이메일 추출
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 이메일로 프렌즈 정보 조회
        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌즈를 찾을 수 없습니다."));

        // 일정 목록 가져오기 (특정 날짜 or 전체 일정)
        List<Task> tasks;
        if (date != null) {
            // 요청한 날짜의 일정만 가져오기
            LocalDate requestedDate = LocalDate.parse(date);
            tasks = taskService.getTasksByFriendAndDate(friend.getId(), requestedDate);
        } else {
            // 전체 일정 가져오기
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

//    @GetMapping
//    public List<PerTaskResponse> getTask(@RequestParam LocalDate date) {
//        List<Task> task = taskService.getTask(date);
//        System.out.println(task);
//        List<PerTaskResponse> list = task.stream().map(
//                v -> PerTaskResponse.builder()
//                        .id(v.getId())
//                        .location(v.getLocation())
//                        .memo(v.getMemo())
//                        .signalTime(v.getSignalTime())
//                        .startTime(v.getStartTime())
//                        .status(v.getStatus())
//                        .taskType(v.getTaskType())
//                        .title(v.getTitle())
//                        .date(date)
//                        .build()
//        ).toList();
//
//        return list;
//    }
//
//    @GetMapping("/all")
//    public List<PerTaskResponse> getAllTask() {
//        List<Task> result = taskService.getAllTask();
//
//        List<PerTaskResponse> list = result.stream().map(
//                v -> PerTaskResponse.builder()
//                        .id(v.getId())
//                        .location(v.getLocation())
//                        .memo(v.getMemo())
//                        .signalTime(v.getSignalTime())
//                        .startTime(v.getStartTime())
//                        .status(v.getStatus())
//                        .taskType(v.getTaskType())
//                        .title(v.getTitle())
//                        .date(v.getDate())
//                        .build()
//        ).toList();
//
//        return list;
//    }
}
