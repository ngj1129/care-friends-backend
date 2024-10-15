package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Location;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.LocationService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationApiController {

    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final LocationService locationService;

    // 위치 정보 업데이트 요청을 처리하는 API
    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LocationUpdateRequest request) {

        String email = userDetails.getUsername();

        // 이메일로 프렌드 정보 가져오기
        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌즈를 찾을 수 없습니다."));

        // 위치 정보 저장
        Location location = new Location();
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setTimestamp(LocalDateTime.now()); // 현재 시간 저장
        location.setFriend(friend);

        // 위치 데이터 저장
        locationService.save(location);

        return ResponseEntity.ok("위치가 업데이트되었습니다.");
    }

    // 보호자가 노약자의 위치 조회
    @GetMapping("/get")
    public List<LocationResponse> getLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID friendId) {

        String email = userDetails.getUsername();

        // 보호자 정보 확인
        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

        // 친구 정보 조회
        Friend friend = friendService.getFriendById(friendId)
                .orElseThrow(() -> new RuntimeException("노약자를 찾을 수 없습니다."));

        // 해당 친구의 모든 위치 데이터 가져오기
        List<Location> locations = locationService.findAll(friend);

        // LocationResponse로 변환하여 반환
        return locations.stream()
                .map(location -> new LocationResponse(location.getId(), location.getLatitude(), location.getLongitude(), location.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Data
    static class LocationUpdateRequest {
        private Double latitude;
        private Double longitude;
        private LocalDateTime timestamp;

        public LocationUpdateRequest(Double latitude, Double longitude, LocalDateTime timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }

    }

    @Data
    public class LocationResponse {
        private Long id;
        private Double latitude;
        private Double longitude;
        private LocalDateTime timestamp;

        public LocationResponse(Long id, Double latitude, Double longitude, LocalDateTime timestamp) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }
    }


}
