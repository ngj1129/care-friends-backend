package hongikchildren.carefriends.task.api;

import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.dto.PerTaskResponse;
import hongikchildren.carefriends.task.dto.UpdateTaskRequest;
import hongikchildren.carefriends.task.service.TaskService;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    // taskID 로 일정 한개만 가져오기
    @GetMapping("/taskID/{taskID}")
    public PerTaskResponse getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskID) {

        // 일정 목록 가져오기 (특정 날짜 or 전체 일정)
        Task task;

        task = taskService.getTaskById(taskID);

        System.out.println(task);

        // 일정 정보를 반환
        return PerTaskResponse.builder()
                .id(task.getId())
                .location(task.getLocation())
                .memo(task.getMemo())
                .signalTime(task.getSignalTime())
                .startTime(task.getStartTime())
                .status(task.getStatus())
                .taskType(task.getTaskType())
                .title(task.getTitle())
                .date(task.getDate())
                .build();

    }

    //    taskID로 일정 수정(제목, 장소, 메모)
    @PutMapping("/{taskId}")
    public void updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequest taskUpdateRequest) {
        taskService.updateTask(taskId, taskUpdateRequest.getTitle(), taskUpdateRequest.getLocation(), taskUpdateRequest.getMemo());
    }

    @DeleteMapping
    public void deleteTask(@RequestBody Map<String, Long> payload) {
        System.out.println(payload.get("id"));
        taskService.deleteTask(payload.get("id"));
    }
}
