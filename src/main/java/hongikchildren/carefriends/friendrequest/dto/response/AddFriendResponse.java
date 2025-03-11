package hongikchildren.carefriends.friendrequest.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AddFriendResponse {
    private UUID friendId;

    public AddFriendResponse(UUID friendId){
        this.friendId = friendId;
    }
}
