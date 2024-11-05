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
public class Task {

    @Id @GeneratedValue
    @Column(name="taskId")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="friendId")
    private Friend friend;

    private Long groupId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    private int period;

    private LocalTime startTime;
    private LocalTime signalTime;
    private String title;
    private String location;
    private String memo;

    @Builder
    public Task(Long id, Long groupId, Friend friend, LocalDate date, LocalTime startTime, LocalTime signalTime, String title,
                String location, String memo, TaskType taskType, Status status, PeriodType periodType, int period) {
        this.id = id;
        this.groupId = groupId;
        this.friend = friend;
        this.date = date;
        this.startTime = startTime;
        this.title = title;
        this.location = location;
        this.memo = memo;
        this.signalTime = signalTime;
        this.taskType = taskType;
        this.status = status;
        this.periodType = periodType;
        this.period = period;
    }

    public void setFriend(Friend friend) { this.friend = friend; }

    public void removeFriend() { this.friend = null; }

}
