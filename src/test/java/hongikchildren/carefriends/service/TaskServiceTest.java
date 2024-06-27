package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.domain.PeriodType;
import hongikchildren.carefriends.domain.Task;
import hongikchildren.carefriends.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testSaveTask() {
        Friend friend = friendService.saveFriend("홍길동", "01001001000", Gender.MALE, LocalDate.now());
        Task task1 = taskService.saveTask(friend, LocalDate.now(), LocalTime.now(), "title", "home", "memo", PeriodType.DAY, 3);
        //Task task2 = taskService.saveTask(friend, LocalDate.now(), LocalTime.now(), "title", "home", "memo");
        Task t1 = taskRepository.findById(task1.getId()).get();
        //Task t2 = taskRepository.findById(task2.getId()).get();
        List<Task> tasks = taskRepository.findByFriend(friend);
        for (Task task : tasks) {
            System.out.println(task.getDate());
        }
        //Assertions.assertEquals(task1, t1);
        //System.out.println(t1.getGroupId());
        //System.out.println(t2.getGroupId());
    }

}
