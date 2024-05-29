package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByFriendAndStatus(Friend friend, String status);
}
