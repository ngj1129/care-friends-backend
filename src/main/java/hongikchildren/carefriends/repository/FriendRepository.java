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

    /*
    프렌드 탈퇴
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f WHERE f.id = :friendId")
    void deleteByFriendId(UUID friendId);


    /*
    보호자 탈퇴 시 프렌드의 보호자를 삭제
     */
    @Modifying
    @Transactional
    @Query("UPDATE Friend f SET f.caregiver = null WHERE f.caregiver.id = :caregiverId")
    void removeCaregiverFromFriends(UUID caregiverId);
}
