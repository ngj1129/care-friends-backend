package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Schedule {
    @Id @GeneratedValue
    @Column(name = "scheduleId")
    private Long id;

    private LocalDate day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId")
    private Friend friend;

    @OneToMany(mappedBy = "schedule")
    private List<Task> tasks = new ArrayList<>();

    @Builder
    protected Schedule(Long id, LocalDate day, Friend friend) {
        this.id = id;
        this.day = day;
        this.friend = friend;
    }
}
