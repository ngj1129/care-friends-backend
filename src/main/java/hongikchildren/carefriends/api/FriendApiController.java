package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.FriendRequest;
import hongikchildren.carefriends.service.FriendRequestService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendRequest")
public class FriendApiController {
    private final FriendRequestService friendRequestService;

    // 친구 추가 요청
    @PostMapping
    public friendResponse addFriend(friendRequest request){
        FriendRequest friendRequest = friendRequestService.sendFriendRequest(request.getCaregiver(), request.getFriendId());
        friendRequestService.acceptFriendRequest(friendRequest.getId());

        return new friendResponse(request.getFriendId());
    }

    @Data
    static class friendRequest{
        private UUID friendId;
        private Caregiver caregiver;


        public friendRequest(UUID friendId, Caregiver caregiver){
            this.friendId = friendId;
            this.caregiver = caregiver;
        }
    }

    @Data
    static class friendResponse{
        private UUID friendId;

        public friendResponse(UUID friendId){
            this.friendId = friendId;
        }
    }

}
