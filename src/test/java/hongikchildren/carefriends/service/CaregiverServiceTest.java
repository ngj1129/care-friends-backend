package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CaregiverServiceTest {
    @Autowired
    private CaregiverService caregiverService;

    @Autowired
    private CaregiverRepository caregiverRepository;


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
    }

    @Test
    void testGetAllCaregivers() {
        // Given
        caregiverService.saveCaregiver("hyeyoon", "010-2222-3333", Gender.FEMALE, LocalDate.of(2001, 11, 29));

        // When
        List<Caregiver> allCaregivers = caregiverService.getAllCaregivers();

        // Then
        assertFalse(allCaregivers.isEmpty());
        assertEquals(1, allCaregivers.size());
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
        Long caregiverId = savedCaregiver.getId();
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
        Long caregiverId = savedCaregiver.getId();

        // When
        caregiverService.deleteCaregiver(caregiverId);

        // Then
        assertFalse(caregiverRepository.findById(caregiverId).isPresent());
    }
}
