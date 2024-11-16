package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Hospital;
import hongikchildren.carefriends.domain.Task;
import hongikchildren.carefriends.domain.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    List<Task> findByFriend(Friend friend);

    List<Task> findByDate(LocalDate date);

    List<Task> findByFriendId(UUID friendId);

    List<Task> findByFriendIdAndDate(UUID friendId, LocalDate date);

    @Modifying
    @Transactional
    @Query("UPDATE Task e SET e.title = :title, e.location = :location, e.memo = :memo WHERE e.id = :id")
    int updateTask(@Param("id") Long id, @Param("title") String title, @Param("location") String location, @Param("memo") String memo);

    //프렌드가 방문한 특정 병원의 진료 목록
    List<Task> findByFriendAndHospitalAndTaskType(Friend friend, Hospital hospital, TaskType taskType);

    //프렌드의 모든 진료 목록
    List<Task> findByFriendAndTaskType(Friend friend, TaskType taskType);
}
