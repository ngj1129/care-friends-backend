package hongikchildren.carefriends.task.dto;

import hongikchildren.carefriends.task.domain.Status;
import hongikchildren.carefriends.task.domain.TaskType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PerTaskResponse {
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
    public PerTaskResponse(Long id, String memo, LocalTime startTime, LocalTime signalTime, String location, String title, Status status, TaskType taskType, LocalDate date) {
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
