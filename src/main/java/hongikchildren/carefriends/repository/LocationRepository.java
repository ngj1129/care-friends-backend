package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findLocationsByFriend(Friend friend);
}
