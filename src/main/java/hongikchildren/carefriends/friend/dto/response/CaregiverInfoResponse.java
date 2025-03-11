package hongikchildren.carefriends.friend.dto.response;

import hongikchildren.carefriends.user.domain.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CaregiverInfoResponse {
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private String profileImg;

    public CaregiverInfoResponse(String name, String phoneNumber, LocalDate birthDate, Gender gender, String profileImg) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profileImg = profileImg;
    }
}
