package hongikchildren.carefriends.friendrequest.repository;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friendrequest.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByFriendAndStatus(Friend friend, String status);
    Optional<FriendRequest> findByCaregiverAndFriendAndStatus(Caregiver caregiver, Friend friend, String status);
    List<FriendRequest> findByCaregiver(Caregiver caregiver);
}
