package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.FriendRequest;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.service.FriendRequestService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendRequest")
public class FriendApiController {
    private final FriendRequestService friendRequestService;
    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;

    // 친구 추가 요청
    @PostMapping
    public AddFriendResponse addFriend(@RequestBody AddFriendRequest request){
        FriendRequest friendRequest = friendRequestService.sendFriendRequest(request.getCaregiver(), request.getFriendId());
        friendRequestService.acceptFriendRequest(friendRequest.getId());

        return new AddFriendResponse(request.getFriendId());
    }

    @GetMapping
    public GetFriendsResponse getFriends(UUID caregiverId, UUID friendId){
        List<UUID> caregiversFriendsIds = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"))
                .getFriends().stream()
                .map(Friend::getId)
                .collect(Collectors.toList());

        UUID friendCaregiverId = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"))
                .getCaregiver().getId();

        return new GetFriendsResponse(caregiversFriendsIds, friendCaregiverId);
    }

    @Data
    static class AddFriendRequest{
        private UUID friendId;
        private Caregiver caregiver;

        public AddFriendRequest() {}

        public AddFriendRequest(UUID friendId, Caregiver caregiver){
            this.friendId = friendId;
            this.caregiver = caregiver;
        }
    }

    @Data
    static class AddFriendResponse{
        private UUID friendId;

        public AddFriendResponse(UUID friendId){
            this.friendId = friendId;
        }
    }

    @Data
    static class GetFriendsResponse {
        private List<UUID> caregiverFriendsIds;
        private UUID friendCaregiverId;

        public GetFriendsResponse(List<UUID> caregiverFriendsIds, UUID friendCaregiverId) {
            this.caregiverFriendsIds = caregiverFriendsIds;
            this.friendCaregiverId = friendCaregiverId;
        }
    }

}
