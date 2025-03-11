package hongikchildren.carefriends.friendrequest.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FriendRequestListResponse {
    private Long requestId;
    private UUID friendId;
    private String friendName;
    private String status;

    public FriendRequestListResponse(Long requestId, UUID friendId, String friendName, String status) {
        this.requestId = requestId;
        this.friendId = friendId;
        this.friendName = friendName;
        this.status = status;
    }
}
