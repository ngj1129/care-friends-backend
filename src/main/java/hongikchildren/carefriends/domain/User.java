package hongikchildren.carefriends.domain;

import java.time.LocalDate;

public interface User {
    String getEmail();
    String getName();

    String getPhoneNumber();
    Gender getGender();
    LocalDate getBirthDate();
    String getProfileImg();

    void setProfileImg(String profileImg);
}
