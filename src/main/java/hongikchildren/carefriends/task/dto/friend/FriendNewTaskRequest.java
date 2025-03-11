package hongikchildren.carefriends.task.dto.friend;

import hongikchildren.carefriends.task.domain.PeriodType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FriendNewTaskRequest {
    private LocalDate date;
    private PeriodType periodType;
    private int period;
    private LocalTime startTime;
    //private LocalTime signalTime; 일단 기본 10분 전 알람
    private String title;
    private String location;
    private String memo;

    public FriendNewTaskRequest(LocalDate date, PeriodType periodType, int period, LocalTime startTime, String title, String location, String memo) {
        this.date = date;
        this.periodType = periodType;
        this.period = period;
        this.startTime = startTime;
        this.title = title;
        this.location = location;
        this.memo = memo;
    }
}
