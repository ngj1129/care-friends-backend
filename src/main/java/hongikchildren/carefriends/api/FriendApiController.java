package hongikchildren.carefriends.api;

import com.amazonaws.services.kms.model.NotFoundException;
import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.FriendRequest;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.FriendRequestRepository;
import hongikchildren.carefriends.service.FriendRequestService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
//        friendRequestService.acceptFriendRequest(friendRequest.getId());

        return new AddFriendResponse(request.getFriendId());
    }

//    @GetMapping("/getFriends/{caregiverId}/{friendId}")
//    public GetFriendsResponse getFriends(UUID caregiverId, UUID friendId){
//        // 특정 caregiver가 관리하는 firned들의 id 조회
//        List<UUID> caregiversFriendsIds = caregiverRepository.findById(caregiverId)
//                .orElseThrow(() -> new RuntimeException("Caregiver not found"))
//                .getFriends().stream()
//                .map(Friend::getId)
//                .collect(Collectors.toList());
//
//        // 특정 friend를 관리하는 caregiver id를 조회
//        UUID friendCaregiverId = friendRepository.findById(friendId)
//                .orElseThrow(() -> new RuntimeException("Friend not found"))
//                .getCaregiver().getId();
//
//        return new GetFriendsResponse(caregiversFriendsIds, friendCaregiverId);
//    }

    // 보호자가 관리하는 모든 프렌즈 Id 조회
    @GetMapping("/getFriends/{caregiverId}")
    public List<FriendInfoResponse> getFriends(@PathVariable UUID caregiverId){
        return caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new NotFoundException("caregiver not found"))
                .getFriends().stream()
                .map(friend -> new FriendInfoResponse(
                        friend.getName(),
                        friend.getPhoneNumber(),
                        friend.getBirthDate(),
                        friend.getGender()
                ))
                .collect(Collectors.toList());
    }

    // 프렌즈의 보호자 정보 조회
    @GetMapping("/getCaregiver/{friendId}")
    public CaregiverInfoResponse getCaregiver(@PathVariable UUID friendId){
        Friend friend =  friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("프렌즈 찾을 수 없음"));

        Caregiver caregiver = friend.getCaregiver();
        return new CaregiverInfoResponse(
                caregiver.getName(),
                caregiver.getPhoneNumber(),
                caregiver.getBirthDate(),
                caregiver.getGender()
        );
    }

    // 친구 요청 수락 api
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId){
        friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 친구 요청 거절 api
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long requestId){
        friendRequestService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 보호자가 관리하는 친구 삭제 api

    // 보호자가 보낸 친구 요청 취소

    // 보호자가 보낸 친구 요청 상태 조회

    // 프렌즈가 대기 중인 친구 요청 조회
    @GetMapping("/pendingRequests/{friendId}")
    public List<FriendRequestResponse> getPendingRequests(@PathVariable UUID friendId){
        List<FriendRequest> pendingRequests = friendRequestService.getPendingRequest(friendId);
        return pendingRequests.stream()
                .map(req -> new FriendRequestResponse(req.getId(), req.getCaregiver().getId(), req.getCaregiver().getName(), req.getStatus()))
                .collect(Collectors.toList());
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

//    @Data
//    static class GetFriendsResponse {
//        private List<UUID> caregiverFriendsIds;
//        private UUID friendCaregiverId;
//
//        public GetFriendsResponse(List<UUID> caregiverFriendsIds, UUID friendCaregiverId) {
//            this.caregiverFriendsIds = caregiverFriendsIds;
//            this.friendCaregiverId = friendCaregiverId;
//        }
//    }

    @Data
    static class FriendRequestResponse {
        private Long requestId;
        private UUID caregiverId;
        private String caregiverName;
        private String status;

        public FriendRequestResponse(Long requestId, UUID caregiverId, String caregiverName, String status) {
            this.requestId = requestId;
            this.caregiverName = caregiverName;
            this.caregiverId = caregiverId;
            this.status = status;
        }
    }

    @Data
    public static class FriendInfoResponse {
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
        private Gender gender;

        public FriendInfoResponse(String name, String phoneNumber, LocalDate birthDate, Gender gender) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.birthDate = birthDate;
            this.gender = gender;
        }
    }

    @Data
    public static class CaregiverInfoResponse {
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
        private Gender gender;

        public CaregiverInfoResponse(String name, String phoneNumber, LocalDate birthDate, Gender gender) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.birthDate = birthDate;
            this.gender = gender;
        }
    }
}
