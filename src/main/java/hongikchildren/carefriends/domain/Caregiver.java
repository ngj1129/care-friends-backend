package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Caregiver {

    @Id @GeneratedValue
    @Column(name = "caregiverId")
    private Long id;

    private String name;

    private String phoneNumber;

    private String gender;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "caregiver")
    private List<Friends> friends =new ArrayList<>();


    @Builder
    protected Caregiver(Long id, String name, String phoneNumber, String gender, LocalDate birthDate, List<Friends> friends) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.friends = friends;
    }
}
