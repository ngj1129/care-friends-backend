package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Caregiver {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "caregiverId")
    private UUID id;

    private String token;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "caregiver")
    private List<Friend> friends =new ArrayList<>();


    @OneToMany(mappedBy = "caregiver")
    private List<FriendRequest> friendRequests = new ArrayList<>();

    @Builder
    protected Caregiver(UUID id, String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }


    // Friends 추가 메서드
    public void addFriend(Friend friend){
        this.friends.add(friend);
        friend.setCaregiver(this); // friends 엔티티의 caregiver 설정
    }

    // Friends 삭제 메서드
    public void removeFriend(Friend friend){
        this.friends.remove(friend);
        friend.removeCaregiver(); // friends 엔티티의 caregiver 초기화
    }

}
