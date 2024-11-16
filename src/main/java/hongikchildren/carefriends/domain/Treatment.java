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
public class Treatment {

    @Id
    @GeneratedValue
    @Column(name = "taskId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId")
    private Friend friend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalTime startTime;

    private String title;

    private String memo;

    @Builder
    public Treatment(Long id, Friend friend, Hospital hospital, LocalDate date, LocalTime startTime, String title,
                     String memo, Status status) {
        this.id = id;
        this.friend = friend;
        this.hospital = hospital;
        this.date = date;
        this.startTime = startTime;
        this.title = title;
        this.memo = memo;
        this.status = status;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public void removeFriend() {
        this.friend = null;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public void removeHospital() {
        this.hospital = null;
    }
}
