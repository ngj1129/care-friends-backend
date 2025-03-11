package hongikchildren.carefriends.friendrequest.dto.response;

import hongikchildren.carefriends.user.domain.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AddFriendInfoResponse {
    private UUID friendId;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private String profileImg;

    public AddFriendInfoResponse(UUID friendId, String name, String phoneNumber, LocalDate birthDate, Gender gender, String profileImg) {
        this.friendId = friendId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profileImg = profileImg;
    }
}
