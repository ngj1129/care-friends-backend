package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    Optional<Caregiver> findByCaregiverId(Long caregiverId);
    List<Caregiver> findByCaregiverName(String caregiverName);

}