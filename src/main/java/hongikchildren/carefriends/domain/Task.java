package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
public class Task {

    @Id @GeneratedValue
    @Column(name="taskId")
    private Long id;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="scheduleId")
//    private Schedule schedule;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="friendId")
    private Friend friend;

    private Long groupId;

    private LocalDate day;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalTime startTime;
    private LocalTime signalTime;
    private String title;
    private String location;
    private String memo;

    @Builder
    public Task(Long id, LocalDate day, Long groupId, LocalTime startTime, LocalTime signalTime, String title, String location, String memo, TaskType taskType, Status status) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.title = title;
        this.location = location;
        this.memo = memo;
        this.signalTime = signalTime;
        this.taskType = taskType;
        this.status = status;
    }

//    public void setSchedule(Schedule schedule) {
//        this.schedule = schedule;
//    }
}
