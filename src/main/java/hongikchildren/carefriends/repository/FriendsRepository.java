package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friends;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    List<Friends> findByName(String name);

    Optional<Friends> findById(Long friendsId);
}
