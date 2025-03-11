package hongikchildren.carefriends.friendrequest.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FriendRequestStatusResponse {
    private Long requestId;
    private UUID caregiverId;
    private String caregiverName;
    private String status;

    public FriendRequestStatusResponse(Long requestId, UUID caregiverId, String caregiverName, String status) {
        this.requestId = requestId;
        this.caregiverName = caregiverName;
        this.caregiverId = caregiverId;
        this.status = status;
    }
}
