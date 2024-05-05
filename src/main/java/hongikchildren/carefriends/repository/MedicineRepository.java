package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findById(Long id);
    List<Medicine> findByName(String name);
}
