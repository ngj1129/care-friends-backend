//package hongikchildren.carefriends.service;
//
//import hongikchildren.carefriends.caregiver.domain.Caregiver;
//import hongikchildren.carefriends.friend.domain.Friend;
//import hongikchildren.carefriends.friendrequest.domain.FriendRequest;
//import hongikchildren.carefriends.friendrequest.service.FriendRequestService;
//import hongikchildren.carefriends.user.domain.Gender;
//import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
//import hongikchildren.carefriends.friend.repository.FriendRepository;
//import hongikchildren.carefriends.friendrequest.repository.FriendRequestRepository;
//import hongikchildren.carefriends.caregiver.service.CaregiverService;
//import hongikchildren.carefriends.friend.service.FriendService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class FriendRequestServiceTest {
//    @Autowired
//    CaregiverRepository caregiverRepository;
//
//    @Autowired
//    FriendRepository friendRepository;
//
//    @Autowired
//    FriendRequestRepository friendRequestRepository;
//
//    @Autowired
//    CaregiverService caregiverService;
//
//    @Autowired
//    FriendService friendService;
//
//    @Autowired
//    FriendRequestService friendRequestService;
//
//    @Test
//    public void testSendRequest(){
//        // given
//        Caregiver caregiver = caregiverService.saveCaregiver("혜윤", "010-7720-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
//        Friend friend = friendService.saveFriend("프렌드1", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));
//
//        // when
//        friendRequestService.sendFriendRequest(caregiver, friend.getId());
//        FriendRequest friendRequest = friendRequestRepository.findAll().get(0);
//
//        // then
//        assertEquals("pending", friendRequest.getStatus());
//
//    }
//
//    @Test
//    public void testAcceptRequest(){
//        // given
//        Caregiver caregiver = caregiverService.saveCaregiver("혜윤", "010-7720-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
//        Friend friend = friendService.saveFriend("프렌드1", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));
//        friendRequestService.sendFriendRequest(caregiver, friend.getId());
//        FriendRequest request = friendRequestRepository.findAll().get(0);
//
//
//        // when
//        friendRequestService.acceptFriendRequest(request.getId());
//
//        // then
//        assertEquals(caregiver, friend.getCaregiver());
//        assertEquals("accepted", request.getStatus());
//        assertEquals(1, caregiver.getFriends().size());
//
//    }
//}
