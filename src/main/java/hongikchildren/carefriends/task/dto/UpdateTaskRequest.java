package hongikchildren.carefriends.task.dto;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private Long id;
    private String title;
    private String location;
    private String memo;

    public UpdateTaskRequest(Long id, String title, String location, String memo) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.memo = memo;
    }
}
