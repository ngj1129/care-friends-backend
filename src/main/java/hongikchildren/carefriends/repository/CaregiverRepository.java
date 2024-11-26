package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaregiverRepository extends JpaRepository<Caregiver, UUID> {
    Optional<Caregiver> findById(UUID id);

    List<Caregiver> findByName(String name);

    Optional<Caregiver> findByEmail(String email);

    /*
    보호자 탈퇴
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Caregiver c WHERE c.id = :caregiverId")
    void deleteByCaregiverId(UUID caregiverId);
}