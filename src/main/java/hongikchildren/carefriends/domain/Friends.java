package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private LocalTime breakfast;
    private LocalTime lunch;
    private LocalTime dinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiverId")
    private Caregiver caregiver;

    @Builder
    public Friends(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate, LocalTime breakfast, LocalTime lunch, LocalTime dinner) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    // Caregiver 설정 메서드
    public void setCaregiver(Caregiver caregiver){
        this.caregiver = caregiver;
    }

    // Caregiver 초기화 메서드
    public void removeCaregiver(){
        this.caregiver = null;
    }


}
