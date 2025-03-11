package hongikchildren.carefriends.task.api.caregiver;

import hongikchildren.carefriends.task.dto.caregiver.CaregiverTaskNotificationRequest;
import hongikchildren.carefriends.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class CaregiverTaskNotificationController {

    private final TaskService taskService;

    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendTaskNotification(@RequestBody CaregiverTaskNotificationRequest request) {
        taskService.sendTaskNotification(request.getTaskId());
        return ResponseEntity.ok("알림이 전송되었습니다.");
    }
}
