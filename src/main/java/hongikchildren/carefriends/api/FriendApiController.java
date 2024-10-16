package hongikchildren.carefriends.api;

import com.amazonaws.services.kms.model.NotFoundException;
import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.jwt.JWTUtil;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.FriendRequestRepository;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendRequestService;
import hongikchildren.carefriends.service.FriendService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JWTUtil jwtUtil;
    private final CaregiverService caregiverService;
    private final FriendService friendService;

    // 친구 추가 요청
    @PostMapping
    public AddFriendResponse addFriend(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddFriendRequest request){
        String email = userDetails.getUsername(); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow();

        FriendRequest friendRequest = friendRequestService.sendFriendRequest(caregiver, request.getFriendId());
        return new AddFriendResponse(request.getFriendId());
    }

    // 보호자가 관리하는 모든 프렌즈 Id 조회
    @GetMapping("/getFriends")
    public List<FriendInfoResponse> getFriends(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername(); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        return caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new NotFoundException("caregiver not found"))
                .getFriends().stream()
                .map(friend -> new FriendInfoResponse(
                        friend.getId(),
                        friend.getName(),
                        friend.getPhoneNumber(),
                        friend.getBirthDate(),
                        friend.getGender()
                ))
                .collect(Collectors.toList());
    }

    // 프렌즈의 보호자 정보 조회
    @GetMapping("/getCaregiver")
    public CaregiverInfoResponse getCaregiver(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);
        Friend friend =  friendRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌즈 찾을 수 없음"));

        Caregiver caregiver = friend.getCaregiver();
        if (caregiver == null) {
            return null; // 보호자가 없을 경우 null 반환
        }

        return new CaregiverInfoResponse(
                caregiver.getName(),
                caregiver.getPhoneNumber(),
                caregiver.getBirthDate(),
                caregiver.getGender()
        );
    }

    // 친구 요청 수락 api
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long requestId) {
        String email = userDetails.getUsername(); // JWT에서 추출된 이메일
        System.out.println("JWT에서 추출된 이메일: " + email);

        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(()-> new RuntimeException("프렌즈를 찾을 수 없습니다."));

        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId);

        if (!friendRequest.getFriend().getId().equals(friend.getId())) {
            throw new RuntimeException("친구 요청 수락 권한이 없습니다.");
        }

        friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }


    // 친구 요청 거절 api
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long requestId){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        friendRequestService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 보호자가 관리하는 친구 삭제 api

    // 보호자가 보낸 친구 요청 취소
    @PostMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancelFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long requestId) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        friendRequestService.cancelFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 보호자의 친구 요청 목록 조회
    @GetMapping("/getRequests")
    public List<FriendRequestListResponse> getRequests(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow();

        List<FriendRequest> requests = friendRequestService.getFriendRequestsByCaregiver(caregiver.getId());
        return requests.stream()
                .map(request -> new FriendRequestListResponse(
                        request.getId(),
                        request.getFriend().getId(),
                        request.getFriend().getName(),
                        request.getStatus()))
                .collect(Collectors.toList());
    }

    // 프렌즈가 대기 중인 친구 요청 조회
    @GetMapping("/pendingRequests")
    public List<FriendRequestResponse> getPendingRequests(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Friend friend = friendService.getFriendByEmail(email).orElseThrow();

        List<FriendRequest> pendingRequests = friendRequestService.getPendingRequest(friend.getId());
        return pendingRequests.stream()
                .map(req -> new FriendRequestResponse(req.getId(), req.getCaregiver().getId(), req.getCaregiver().getName(), req.getStatus()))
                .collect(Collectors.toList());
    }

    // 프렌드 찾기
    @GetMapping("/searchFriend/{uuid}")
    public ResponseEntity<FriendInfoResponse> searhFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID uuid){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Friend friend = friendRepository.findById(uuid)
                .orElseThrow(()-> new NotFoundException("friend not found"));
        FriendInfoResponse friendInfoResponse = new FriendInfoResponse(
                friend.getId(),
                friend.getName(),
                friend.getPhoneNumber(),
                friend.getBirthDate(),
                friend.getGender()
        );
        return ResponseEntity.ok(friendInfoResponse);
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
    public static class FriendRequestListResponse {
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

    @Data
    public static class FriendInfoResponse {
        private UUID friendId;
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
        private Gender gender;

        public FriendInfoResponse(UUID friendId, String name, String phoneNumber, LocalDate birthDate, Gender gender) {
            this.friendId = friendId;
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
