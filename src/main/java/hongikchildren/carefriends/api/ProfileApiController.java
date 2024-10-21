package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.s3.S3Uploader;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileApiController {
    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final FriendService friendService;
    private final CaregiverService caregiverService;

    /*
    내정보
    - 사진 -> 누르면 업로드/수정가능
    - UUID
    - 이름
    - 전화번호
    - 생년월일
    - 성별
     */
    @GetMapping
    public ProfileResponse getProfileInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        User user = userService.getUserByEmail(email);
        return new ProfileResponse(user.getProfileImg(), user.getId(), user.getName(), user.getPhoneNumber(), user.getBirthDate(), user.getGender());
    }

    @PostMapping("/img")
    public ProfileResponse saveMedicine(@AuthenticationPrincipal UserDetails userDetails, @RequestPart(required = false) MultipartFile image) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        String getImageUrl = "null";
        try {
            getImageUrl = s3Uploader.uploadFiles(image, "profile");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        User user = userService.setProfile(email, getImageUrl);

        return new ProfileResponse(user.getProfileImg(), user.getId(), user.getName(), user.getPhoneNumber(), user.getBirthDate(), user.getGender());
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateProfileRequest request){
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return new ResponseEntity<>("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        // 생년월일 파싱
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(request.getBirthDate()); // 문자열을 LocalDate로 변환
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid birth date format", HttpStatus.BAD_REQUEST);
        }

        if ("friend".equalsIgnoreCase(request.getUserType())) {
            friendService.updateFriend(
                    user.getId(),
                    request.getName(),
                    request.getPhoneNumber(),
                    Gender.valueOf(request.getGender().toUpperCase()),
                    birthDate
            );
        } else if ("caregiver".equalsIgnoreCase(request.getUserType())) {
            caregiverService.updateCaregiver(
                    user.getId(),
                    request.getName(),
                    request.getPhoneNumber(),
                    Gender.valueOf(request.getGender().toUpperCase()),
                    birthDate
            );
        } else {
            return new ResponseEntity<>("잘못된 사용자 유형입니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("프로필이 성공적으로 수정되었습니다.", HttpStatus.OK);
    }


    @Data
    public static class ProfileResponse {
        private String profileImg;
        private UUID uuid;
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
        private Gender gender;

        public ProfileResponse(String profileImg, UUID uuid, String name, String phoneNumber, LocalDate birthDate, Gender gender) {
            this.profileImg = profileImg;
            this.uuid = uuid;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.birthDate = birthDate;
            this.gender = gender;
        }
    }

    @Data
    public static class UpdateProfileRequest {
        private String userType;
        private String name;
        private String phoneNumber;
        private String birthDate;
        private String gender;
    }
}
