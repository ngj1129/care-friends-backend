package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     *
     * @param friend
     * @param date
     * @param startTime
     * @param title
     * @param location
     * @param memo
     * @param periodType NONE, DAY, WEEK, MONTH, YEAR
     * @param period
     * @return
     * signalTime: 시작시간 10분 전
     */
    @Transactional
    public Task saveTask(Friend friend, LocalDate date, LocalTime startTime, String title, String location, String memo,
                         PeriodType periodType, int period) {

        long groupId = System.currentTimeMillis();

        Task task = Task.builder()
                .groupId(groupId)
                .friend(friend)
                .date(date)
                .startTime(startTime)
                .signalTime(startTime.minusMinutes(10))
                .title(title)
                .location(location)
                .memo(memo)
                .taskType(TaskType.JOB)
                .status(Status.YET)
                .periodType(periodType)
                .period(period)
                .build();

        if (periodType != PeriodType.NONE) {
            createCustomTask(task, periodType, period);
        }

        return taskRepository.save(task);
    }

    public void createCustomTask(Task task, PeriodType periodType, int period) {
        LocalDate startDate = task.getDate();
        for (int i = 1; i <= period; i++) {
            LocalDate newDate = switch (periodType) {
                case DAY -> startDate.plusDays(i);
                case WEEK -> startDate.plusWeeks(i);
                case MONTH -> startDate.plusMonths(i);
                case YEAR -> startDate.plusYears(i);
                default -> null;
            };

            if (newDate != null) {
                Task newTask = Task.builder()
                        .groupId(task.getGroupId())
                        .friend(task.getFriend())
                        .date(newDate)
                        .startTime(task.getStartTime())
                        .signalTime(task.getSignalTime())
                        .title(task.getTitle())
                        .location(task.getLocation())
                        .memo(task.getMemo())
                        .taskType(task.getTaskType())
                        .status(task.getStatus())
                        .periodType(task.getPeriodType())
                        .period(task.getPeriod())
                        .build();

                taskRepository.save(newTask);
            }
        }
    }
}
