package hongikchildren.carefriends.task.dto;

import lombok.Data;

@Data
public class NewTaskResponse {
    private long id; //이 id는 그룹 id이다.

    public NewTaskResponse(long id) {
        this.id = id;
    }
}
