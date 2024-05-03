package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Friends {

    @Id @GeneratedValue
    @Column(name = "friendsId")
    private Long id;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

}
