package hongikchildren.carefriends.user.api;

import hongikchildren.carefriends.infra.s3.S3Service;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.user.dto.request.UpdateProfileRequest;
import hongikchildren.carefriends.user.service.UserService;
import hongikchildren.carefriends.user.domain.Gender;
import hongikchildren.carefriends.user.domain.User;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.user.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileApiController {
    private final UserService userService;
    private final S3Service s3Service;
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
    public ProfileResponse saveProfileImg(@AuthenticationPrincipal UserDetails userDetails, @RequestPart(required = false) MultipartFile image) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        String getImageUrl = "null";
        try {
            getImageUrl = s3Service.uploadFiles(image, "profile");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        User user = userService.setProfile(email, getImageUrl);

        return new ProfileResponse(user.getProfileImg(), user.getId(), user.getName(), user.getPhoneNumber(), user.getBirthDate(), user.getGender());
    }

    @DeleteMapping("/img")
    public ResponseEntity<String> deleteProfileImg(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 사용자 조회
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        String profileImgUrl = user.getProfileImg();
        if (profileImgUrl == null || profileImgUrl.isEmpty()) {
            return new ResponseEntity<>("삭제할 프로필 이미지가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            // S3Uploader를 사용해 S3에서 파일 삭제
            s3Service.deleteFile(profileImgUrl, "profile");

            // 데이터베이스 업데이트 (프로필 이미지 URL을 null로 설정)
            userService.setProfile(email, null);

            return new ResponseEntity<>("프로필 이미지가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("프로필 이미지 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
}
