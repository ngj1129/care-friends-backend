package hongikchildren.carefriends.user.api;

import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.user.service.UserService;
import hongikchildren.carefriends.user.domain.Gender;
import hongikchildren.carefriends.user.domain.User;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.user.dto.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest requestDto) {
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

}

