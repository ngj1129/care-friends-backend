package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface FriendRepository extends JpaRepository<Friend, UUID> {
    List<Friend> findByName(String name);

    Optional<Friend> findById(UUID friendId);

    Optional<Friend> findByEmail(String email);

    // friendId로 Friend 엔티티 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f WHERE f.id = :friendId")
    void deleteByFriendId(UUID friendId);
}
