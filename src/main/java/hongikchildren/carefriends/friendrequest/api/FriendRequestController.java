package hongikchildren.carefriends.friendrequest.api;

import com.amazonaws.services.kms.model.NotFoundException;
import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.repository.FriendRepository;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.friendrequest.domain.FriendRequest;
import hongikchildren.carefriends.friendrequest.dto.request.AddFriendRequest;
import hongikchildren.carefriends.friendrequest.dto.response.AddFriendInfoResponse;
import hongikchildren.carefriends.friendrequest.dto.response.AddFriendResponse;
import hongikchildren.carefriends.friendrequest.dto.response.FriendRequestListResponse;
import hongikchildren.carefriends.friendrequest.dto.response.FriendRequestStatusResponse;
import hongikchildren.carefriends.friendrequest.service.FriendRequestService;
import hongikchildren.carefriends.infra.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendRequest")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;
    private final JWTUtil jwtUtil;
    private final CaregiverService caregiverService;
    private final FriendService friendService;

    // 친구 요청을 위해 프렌드 찾기
    @GetMapping("/searchFriend/{uuid}")
    public ResponseEntity<AddFriendInfoResponse> searhFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID uuid){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Friend friend = friendRepository.findById(uuid)
                .orElseThrow(()-> new NotFoundException("friend not found"));
        AddFriendInfoResponse friendInfoResponse = new AddFriendInfoResponse(
                friend.getId(),
                friend.getName(),
                friend.getPhoneNumber(),
                friend.getBirthDate(),
                friend.getGender(),
                friend.getProfileImg()
        );
        return ResponseEntity.ok(friendInfoResponse);
    }

    // 친구 추가 요청
    @PostMapping
    public AddFriendResponse addFriend(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddFriendRequest request){
        String email = userDetails.getUsername(); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow();

        FriendRequest friendRequest = friendRequestService.sendFriendRequest(caregiver, request.getFriendId());
        return new AddFriendResponse(request.getFriendId());
    }

    // 프렌즈가 현재 친구 요청 조회
    @GetMapping("/pendingRequests")
    public List<FriendRequestStatusResponse> getPendingRequests(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        Friend friend = friendService.getFriendByEmail(email).orElseThrow();

        List<FriendRequest> pendingRequests = friendRequestService.getPendingRequest(friend.getId());
        return pendingRequests.stream()
                .map(req -> new FriendRequestStatusResponse(req.getId(), req.getCaregiver().getId(), req.getCaregiver().getName(), req.getStatus()))
                .collect(Collectors.toList());
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

    // 보호자가 보낸 친구 요청 취소
    @PostMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancelFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long requestId) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        friendRequestService.cancelFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
