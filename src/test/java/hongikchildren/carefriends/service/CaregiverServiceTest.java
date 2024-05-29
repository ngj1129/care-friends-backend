package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CaregiverServiceTest {

    // JUnit으로 테스트를 수행하게 되면 매개변수 관리는 스프링 컨테이너가 아닌 테스트 프레임워크인 junit 스스로가 지원하여
    // RequiredArgsConstructor 어노테이션 사용 불가. Autowired 써야함
    @Autowired
    private CaregiverService caregiverService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private CaregiverRepository caregiverRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Test
    public void testSaveCaregiver(){
        // given
        String name = "정혜윤";
        String phoneNumber = "010-7720-5751";
        Gender gender = Gender.FEMALE;
        LocalDate birthDate = LocalDate.of(2001,11,29);

        // when
        Caregiver savedCaregiver = caregiverService.saveCaregiver(name, phoneNumber, gender, birthDate);

        // then
        assertNotNull(savedCaregiver.getId());
        assertEquals(name, savedCaregiver.getName());
        assertEquals(phoneNumber, savedCaregiver.getPhoneNumber());
        assertEquals(gender, savedCaregiver.getGender());
        assertEquals(birthDate, savedCaregiver.getBirthDate());
        System.out.println(savedCaregiver.getId());
    }

    @Test
    void testGetAllCaregivers() {
        // Given
        caregiverService.saveCaregiver("hyeyoon", "010-2222-3333", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        caregiverService.saveCaregiver("윤혜정", "010-4321-2141", Gender.FEMALE, LocalDate.of(2001, 9, 2));

        // When
        List<Caregiver> allCaregivers = caregiverService.getAllCaregivers();

        // Then
        assertFalse(allCaregivers.isEmpty());
        assertEquals(2, allCaregivers.size());
    }

    @Test
    void testGetCaregiverById() {
        // Given
        Caregiver savedCaregiver = caregiverService.saveCaregiver("yoon", "010-5553-2222", Gender.MALE, LocalDate.of(2000, 5, 15));

        // When
        Optional<Caregiver> optionalCaregiver = caregiverService.getCaregiverById(savedCaregiver.getId());

        // Then
        assertTrue(optionalCaregiver.isPresent());
        assertEquals("yoon", optionalCaregiver.get().getName());
    }

    @Test
    void testUpdateCaregiver() {
        // Given
        Caregiver savedCaregiver = caregiverService.saveCaregiver("kim", "010-3342-2142", Gender.MALE, LocalDate.of(1980, 5, 15));
        UUID caregiverId = savedCaregiver.getId();
        String newName = "park";
        String newPhoneNumber = "010-2324-2313";

        // When
        caregiverService.updateCaregiver(caregiverId, newName, newPhoneNumber, Gender.MALE, LocalDate.of(1980, 5, 15));

        // Then
        Optional<Caregiver> updatedCaregiver = caregiverRepository.findById(caregiverId);
        assertTrue(updatedCaregiver.isPresent());
        assertEquals(newName, updatedCaregiver.get().getName());
        assertEquals(newPhoneNumber, updatedCaregiver.get().getPhoneNumber());
    }

    @Test
    void testDeleteCaregiver() {
        // Given
        Caregiver savedCaregiver = caregiverService.saveCaregiver("jeong", "010-1234-1234", Gender.MALE, LocalDate.of(2001, 5, 4));
        UUID caregiverId = savedCaregiver.getId();

        // When
        caregiverService.deleteCaregiver(caregiverId);

        // Then
        assertFalse(caregiverRepository.findById(caregiverId).isPresent());
    }

    @Test
    public void testAddFriendToCaregiver(){
        // Given
        Caregiver caregiver1 = caregiverService.saveCaregiver("hyeyoon", "010-2222-3333", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Caregiver caregiver2 = caregiverService.saveCaregiver("caregiver2", "010-2222-3333", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Friend friend1 = friendService.saveFriend("kim", "0129-2312", Gender.FEMALE, LocalDate.of(2000,11,11));
        Friend friend2 = friendService.saveFriend("jeong", "0113-2332", Gender.MALE, LocalDate.of(1999,1,11));
        Friend friend3 = friendService.saveFriend("friends3", "0113-2332", Gender.MALE, LocalDate.of(1999,1,11));


        // when
        caregiverService.addFriendToCaregiver(caregiver1.getId(), friend1);
        caregiverService.addFriendToCaregiver(caregiver1.getId(), friend2);
        caregiverService.addFriendToCaregiver(caregiver1.getId(), friend3);

        // then
        assertEquals(caregiver1.getName(), friend1.getCaregiver().getName());
        assertEquals(caregiver1.getName(), friend2.getCaregiver().getName());
        assertEquals(3, caregiver1.getFriends().size());
        // 이미 caregiver1에게 등록된 friend3을 caregiver2에게 등록할 때 예외 발생 확인
        assertThrows(RuntimeException.class, () -> caregiverService.addFriendToCaregiver(caregiver2.getId(), friend3));
    }

    @Test
    public void testDeleteFriendFromCaregiver(){
        // Given
        Caregiver caregiver = caregiverService.saveCaregiver("hyeyoon", "010-2222-3333", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Friend friend1 = friendService.saveFriend("kim", "0129-2312", Gender.FEMALE, LocalDate.of(2000,11,11));
        Friend friend2 = friendService.saveFriend("jeong", "0113-2332", Gender.MALE, LocalDate.of(1999,1,11));

        caregiverService.addFriendToCaregiver(caregiver.getId(), friend1);
        caregiverService.addFriendToCaregiver(caregiver.getId(), friend2);

        // when
        caregiverService.deleteFriendFromCaregiver(caregiver.getId(), friend1);

        // then
        assertEquals(1, caregiver.getFriends().size());
        assertEquals(friend2, caregiver.getFriends().get(0));
        assertNull(friend1.getCaregiver());
    }
}
