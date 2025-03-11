package hongikchildren.carefriends.location.repository;

import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findLocationsByFriend(Friend friend);
}
