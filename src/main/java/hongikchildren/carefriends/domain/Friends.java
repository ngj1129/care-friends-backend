package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
public class Friends {

    @Id @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    private String name;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

}
