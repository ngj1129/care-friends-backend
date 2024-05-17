package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Friend;
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
public class FriendServiceTest {
    @Autowired
    private FriendService friendService;

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
        Friend savedFriend = friendService.saveFriends(name, phoneNumber, gender, birthDate);

        // then
        assertNotNull(savedFriend.getId());
        assertEquals(name, savedFriend.getName());
        assertEquals(phoneNumber, savedFriend.getPhoneNumber());
        assertEquals(gender, savedFriend.getGender());
        assertEquals(birthDate, savedFriend.getBirthDate());

    }

}
