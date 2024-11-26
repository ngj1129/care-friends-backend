package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.fcm.FcmSendDto;
import hongikchildren.carefriends.fcm.FcmServiceImpl;
import hongikchildren.carefriends.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final FcmServiceImpl fcmServiceImpl;

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
        for (int i = 1; i < period; i++) {
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

    @Transactional
    public void sendTaskNotification(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new RuntimeException("일정을 찾을 수 없습니다."));

        Friend friend = task.getFriend();
        // 시간 형식 지정 (예: 12시 00분)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH시 mm분");
        String formattedTime = task.getStartTime().format(timeFormatter);

        // FCM 알림 전송
        FcmSendDto fcmSendDto = FcmSendDto.builder()
                .id(friend.getId())
                .title("일정을 확인하세요")
                .body("오늘 '" + task.getTitle() + "'일정이 " + formattedTime + "에 있습니다.")
                .receiverType("Friend")
                .build();

        try {
            fcmServiceImpl.sendMessageTo(fcmSendDto);
            System.out.println("FCM 메시지 전송 성공: " + fcmSendDto.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FCM 메시지 전송 실패");
            throw new RuntimeException("FCM 메시지 전송에 실패했습니다.");
        }
    }


    @Transactional
    public void setTaskStatusToDone(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        task.updateStatus(Status.DONE);
    }

    public List<Task> getTask(LocalDate currentDate) {
        List<Task> result = taskRepository.findByDate(currentDate);
        return result;
    }

    public List<Task> getAllTask(){
        List<Task> result = taskRepository.findAll();
        return result;
    }

    public List<Task> getTasksByFriend(UUID friendId) {
        return taskRepository.findByFriendId(friendId);
    }

    public Task getTaskById(Long id) {return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));}

    public List<Task> getTasksByFriendAndDate(UUID friendId, LocalDate date) {
        return taskRepository.findByFriendIdAndDate(friendId, date);
    }


    public int updateTask(Long id, String title,String location, String memo){
        return taskRepository.updateTask(id, title,location, memo);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
