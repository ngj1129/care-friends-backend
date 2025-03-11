package hongikchildren.carefriends.infra.redis.dto;


import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.user.domain.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FriendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private String profileImg;
    private String fcmToken;
    private LocalDate birthDate;

    public FriendDTO(Friend friend) {
        this.id = friend.getId();
        this.name = friend.getName();
        this.gender = friend.getGender();
        this.phoneNumber = friend.getPhoneNumber();
        this.email = friend.getEmail();
        this.profileImg = friend.getProfileImg();
        this.fcmToken = friend.getFcmToken();
        this.birthDate = friend.getBirthDate();
    }
}