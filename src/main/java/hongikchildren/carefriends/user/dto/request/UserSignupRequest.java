package hongikchildren.carefriends.user.dto.request;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String email;
    private String userType; // 'caregiver' 또는 'friend'
    private String name;
    private String phone;
    private String gender; // 'MALE' 또는 'FEMALE'
    private String birthDate; // 문자열 형태로 받아서 LocalDate로 변환
    private String fcmToken;
}
