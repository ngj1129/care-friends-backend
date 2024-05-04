package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Friends;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.FriendsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class FriendsServiceTest {
    @Autowired
    private FriendsService friendsService;

    @Autowired
    private FriendsRepository friendsRepository;

    @Test
    public void testSaveFriends(){
        // given
        String name = "김이박";
        String phoneNumber = "010-0000-2222";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.of(1999, 11, 22);

        // when
        Friends savedFriends = friendsService.saveFriends(name, phoneNumber, gender, birthDate);

        // then
        assertNotNull(savedFriends.getId());
        assertEquals(name, savedFriends.getName());
        assertEquals(phoneNumber, savedFriends.getPhoneNumber());
        assertEquals(gender, savedFriends.getGender());
        assertEquals(birthDate, savedFriends.getBirthDate());

    }

}
