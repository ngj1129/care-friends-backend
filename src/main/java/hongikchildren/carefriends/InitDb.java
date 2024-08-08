package hongikchildren.carefriends;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.service.FriendService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit() {

//            @org.springframework.transaction.annotation.Transactional
//            public Friend saveFriend(String name, String phoneNumber, Gender gender, LocalDate birthDate) {
//                Friend friend = Friend.builder()
//                        .name(name)
//                        .phoneNumber(phoneNumber)
//                        .gender(gender)
//                        .birthDate(birthDate)
//                        .build();
//                return friendRepository.save(friend);
//            }

            Friend f1 = createFriend(Gender.FEMALE, "나경주", "01085335204");
            em.persist(f1);
            System.out.println(f1.getId());

            Caregiver c1 = createCaregiver(Gender.MALE, "보호자", "01012345678");
            em.persist(c1);
            System.out.println(c1.getId());

        }

        private static Friend createFriend(Gender gender, String name, String phoneNumber) {
            Friend friend = new Friend();
            friend.setGender(gender);
            friend.setName(name);
            friend.setPhoneNumber(phoneNumber);
            return friend;
        }

//        // Caregiver 저장
//        @org.springframework.transaction.annotation.Transactional
//        public Caregiver saveCaregiver(String name, String phoneNum, Gender gender, LocalDate birthDate){
//            Caregiver caregiver = Caregiver.builder()
//                    .name(name)
//                    .phoneNumber(phoneNum)
//                    .gender(gender)
//                    .birthDate(birthDate)
//                    .build();
//            return caregiverRepository.save(caregiver);
//
//        }

        private static Caregiver createCaregiver(Gender gender, String name, String phoneNumber) {
            Caregiver caregiver = new Caregiver();
            caregiver.setGender(gender);
            caregiver.setName(name);
            caregiver.setPhoneNumber(phoneNumber);
            return caregiver;
        }
    }
}
