package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Task;
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

    @Modifying
    @Transactional
    @Query("UPDATE Task e SET e.title = :title, e.memo = :memo WHERE e.id = :id")
    int updateTask(@Param("id") Long id, @Param("title") String title, @Param("memo") String memo);
}
