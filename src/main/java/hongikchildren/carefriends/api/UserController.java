package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final FriendService friendService;
    private final CaregiverService caregiverService;

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

