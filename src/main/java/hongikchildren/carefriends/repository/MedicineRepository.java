package hongikchildren.carefriends.repository;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findById(Long id);
    List<Medicine> findByName(String name);

    //spring data jpa가 프록시 객체 생성하고 조인 쿼리 생성해줌.
    List<Medicine> findByFriend(Friend friend);
}
