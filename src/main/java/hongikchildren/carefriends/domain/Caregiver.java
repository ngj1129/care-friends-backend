package hongikchildren.carefriends.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
public class Caregiver {

    private Long id;
    private String name;
    private String phoneNumber;
    private Gender gender;
    private LocalDate birthDate;

    @Builder
    protected Caregiver(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}
