package hongikchildren.carefriends.task.dto.caregiver;

import lombok.Data;

@Data
public class CaregiverTaskNotificationRequest {
    private Long taskId;

    public CaregiverTaskNotificationRequest() {
    }

    public CaregiverTaskNotificationRequest(Long taskId) {
        this.taskId = taskId;
    }
}
