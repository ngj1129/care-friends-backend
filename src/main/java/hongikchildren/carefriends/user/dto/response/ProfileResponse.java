package hongikchildren.carefriends.user.dto.response;

import hongikchildren.carefriends.user.domain.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProfileResponse {
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
