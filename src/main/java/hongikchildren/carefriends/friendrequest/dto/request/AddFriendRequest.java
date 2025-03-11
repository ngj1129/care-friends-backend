package hongikchildren.carefriends.friendrequest.dto.request;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import lombok.Data;

import java.util.UUID;

@Data
public class AddFriendRequest {
    private UUID friendId;
    private Caregiver caregiver;

    public AddFriendRequest() {}

    public AddFriendRequest(UUID friendId, Caregiver caregiver){
        this.friendId = friendId;
        this.caregiver = caregiver;
    }
}
