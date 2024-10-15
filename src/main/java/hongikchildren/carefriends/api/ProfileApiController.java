package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.domain.Medicine;
import hongikchildren.carefriends.domain.TakeTime;
import hongikchildren.carefriends.domain.User;
import hongikchildren.carefriends.s3.S3Uploader;
import hongikchildren.carefriends.service.MedicineService;
import hongikchildren.carefriends.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileApiController {
    private final UserService userService;
    private final S3Uploader s3Uploader;

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
}
