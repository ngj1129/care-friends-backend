package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Status;
import hongikchildren.carefriends.domain.Task;
import hongikchildren.carefriends.domain.TaskType;
import hongikchildren.carefriends.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

/*
    @Id @GeneratedValue
    @Column(name="taskId")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="scheduleId")
    private Schedule schedule;

    private Long groupId;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalTime startTime;
    private LocalTime signalTime;
    private String title;
    private String location;
    private String memo;
     */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    //private final ScheduleRepository scheduleRepository;

    //Task 추가. 알림시간은 시작시간 10분전으로 설정됨.
    //date: 선택한 날짜
    @Transactional
    public Task saveTask(Friend friend, LocalDate day, LocalTime startTime, String title, String location, String memo, LocalDate date) {
        Task task = Task.builder()
                .friend(friend)
                .day(day)
                .startTime(startTime)
                .signalTime(startTime.minusMinutes(10))
                .title(title)
                .location(location)
                .memo(memo)
                .taskType(TaskType.JOB)
                .status(Status.YET)
                .build();
//
//        Schedule schedule;
//        if (scheduleRepository.findByDay(date).isPresent()) {
//            //원래 있던 스케줄 가져옴
//            schedule = scheduleRepository.findByDay(date).get();
//        }
//        else {
//            //스케줄 새로 생성
//            schedule = Schedule.builder()
//                    .day(date)
//                    .build();
//            scheduleRepository.save(schedule);
//        }
//        schedule.addTask(task);
//        task.setSchedule(schedule);
        return taskRepository.save(task);
    }

}
