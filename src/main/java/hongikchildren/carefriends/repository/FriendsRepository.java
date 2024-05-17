package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByName(String name);

    Optional<Friend> findById(Long friendId);
}
