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
public class Hospital {

    @Id @GeneratedValue
    @Column(name = "hospitalId")
    private Long id;

    private String name;

    private String link;

    private String address;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId")
    private Friend friend;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public Hospital(Long id, String name, String address, String phone, String link, Friend friend) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.link = link;
        this.friend = friend;
    }

    public void addTreatment(Task task){
        this.tasks.add(task);
        task.setHospital(this); // Task 엔티티의 Friend 설정
    }

    public void removeTreatment(Task task){
        this.tasks.remove(task);
        task.removeHospital(); // Task 엔티티의 Friend 초기화
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public void removeFriend() {
        this.friend = null;
    }
}
