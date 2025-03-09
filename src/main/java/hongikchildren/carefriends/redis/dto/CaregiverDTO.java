package hongikchildren.carefriends.redis.dto;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CaregiverDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 추가

    private UUID id;
    private String name;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private String profileImg;
    private String fcmToken;
    private LocalDate birthDate;
    private List<FriendDTO> friends;

    public CaregiverDTO(Caregiver caregiver) {
        this.id = caregiver.getId();
        this.name = caregiver.getName();
        this.gender = caregiver.getGender();
        this.phoneNumber = caregiver.getPhoneNumber();
        this.email = caregiver.getEmail();
        this.profileImg = caregiver.getProfileImg();
        this.fcmToken = caregiver.getFcmToken();
        this.birthDate = caregiver.getBirthDate();
        this.friends = caregiver.getFriends().stream()
                .map(FriendDTO::new)
                .collect(Collectors.toList());
    }
}
