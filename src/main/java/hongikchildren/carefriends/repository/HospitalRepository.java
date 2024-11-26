package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByLink(String link);

    List<Hospital> findAllByFriend(Friend friend);


    @Modifying
    @Transactional
    @Query("DELETE FROM Hospital h WHERE h.friend.id = :friendId")
    void deleteByFriendId(@Param("friendId") UUID friendId);
}
