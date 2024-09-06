package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface FriendRepository extends JpaRepository<Friend, UUID> {
    List<Friend> findByName(String name);

    Optional<Friend> findById(UUID friendId);

    Optional<Friend> findByEmail(String email);
}
