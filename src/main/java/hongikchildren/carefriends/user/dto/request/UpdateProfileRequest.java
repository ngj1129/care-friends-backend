package hongikchildren.carefriends.user.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String userType;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private String gender;
}
