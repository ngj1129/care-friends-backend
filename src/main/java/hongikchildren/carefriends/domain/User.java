package hongikchildren.carefriends.domain;

import java.time.LocalDate;
import java.util.UUID;

public interface User {
    String getEmail();
    String getName();
    UUID getId();
    String getPhoneNumber();
    Gender getGender();
    LocalDate getBirthDate();
    String getProfileImg();

    void setProfileImg(String profileImg);
}
