package hongikchildren.carefriends.task.service;

import hongikchildren.carefriends.hospital.Hospital;
import hongikchildren.carefriends.task.repository.TaskRepository;
import hongikchildren.carefriends.task.domain.Status;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.domain.TaskType;
import hongikchildren.carefriends.friend.domain.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TreatmentService {

    private final TaskRepository taskRepository;

    /**
     *
     * @param friend
     * @param hospital
     * @param date
     * @param startTime
     * @param title
     * @param memo
     */
    @Transactional
    public Task saveTreatment(Friend friend, Hospital hospital, LocalDate date, LocalTime startTime, String title, String location, String memo) {

        //병원객체있는지확인 -> 없으면만들고 생성해야됨
        Task treatment = Task.builder()
                .friend(friend)
                .hospital(hospital)
                .date(date)
                .startTime(startTime)
                .title(title)
                .location(location)
                .memo(memo)
                .taskType(TaskType.TREATMENT)
                .status(Status.YET)
                .build();

        return taskRepository.save(treatment);
    }

    public List<Task> getTreatmentsByHospitalAndFriend(Hospital hospital, Friend friend) {
        return taskRepository.findByFriendAndHospitalAndTaskType(friend, hospital, TaskType.TREATMENT);
    }

    public List<Task> getTreatmentByFriend(Friend friend) {
        return taskRepository.findByFriendAndTaskType(friend, TaskType.TREATMENT);
    }

    //삭제
    @Transactional
    public void deleteTreatment(Long id) { taskRepository.deleteById(id); }

}
