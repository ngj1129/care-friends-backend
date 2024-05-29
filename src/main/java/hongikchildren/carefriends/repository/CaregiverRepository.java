package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaregiverRepository extends JpaRepository<Caregiver, UUID> {
    Optional<Caregiver> findById(UUID id);
    List<Caregiver> findByName(String name);

}