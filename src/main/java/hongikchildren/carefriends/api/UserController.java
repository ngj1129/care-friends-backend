package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.domain.User;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.UserService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequestDto requestDto) {
        // 생년월일 파싱
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(requestDto.getBirthDate()); // 문자열을 LocalDate로 변환
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid birth date format", HttpStatus.BAD_REQUEST);
        }

        if ("caregiver".equalsIgnoreCase(requestDto.getUserType())) {
            // Caregiver 저장 로직
            caregiverService.saveCaregiver(
                    requestDto.getName(),
                    requestDto.getPhone(),
                    requestDto.getEmail(),
                    Gender.valueOf(requestDto.getGender().toUpperCase()),
                    birthDate,
                    requestDto.getFcmToken()
            );
        } else if ("friend".equalsIgnoreCase(requestDto.getUserType())) {
            // Friend 저장 로직
            friendService.saveFriend(
                    requestDto.getName(),
                    requestDto.getPhone(),
                    requestDto.getEmail(),
                    Gender.valueOf(requestDto.getGender().toUpperCase()),
                    birthDate,
                    requestDto.getFcmToken()
            );
        } else {
            System.out.println("Invalid user type");
            return new ResponseEntity<>("Invalid user type", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*
    회원 탈퇴
     */
    @DeleteMapping("/unregister/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername(); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 사용자 조회
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 반환
        }

        try {
            // 사용자 삭제 로직 호출
            userService.unregister(user);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        } catch (Exception e) {
            // 예외 로그 출력
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 반환
        }
    }

    @Data
    public static class UserSignupRequestDto {
        private String email;
        private String userType; // 'caregiver' 또는 'friend'
        private String name;
        private String phone;
        private String gender; // 'MALE' 또는 'FEMALE'
        private String birthDate; // 문자열 형태로 받아서 LocalDate로 변환
        private String fcmToken;
    }
}

