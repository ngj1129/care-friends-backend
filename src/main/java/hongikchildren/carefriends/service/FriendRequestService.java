    package hongikchildren.carefriends.service;


    import hongikchildren.carefriends.domain.Caregiver;
    import hongikchildren.carefriends.domain.Friend;
    import hongikchildren.carefriends.domain.FriendRequest;
    import hongikchildren.carefriends.fcm.FcmSendDto;
    import hongikchildren.carefriends.fcm.FcmServiceImpl;
    import hongikchildren.carefriends.repository.CaregiverRepository;
    import hongikchildren.carefriends.repository.FriendRepository;
    import hongikchildren.carefriends.repository.FriendRequestRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.io.IOException;
    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

    @Service
    @Transactional(readOnly = true)
    @RequiredArgsConstructor
    public class FriendRequestService {

        private final CaregiverRepository caregiverRepository;
        private final FriendRepository friendRepository;
        private final FriendRequestRepository friendRequestRepository;
        private final FcmServiceImpl fcmServiceImpl;


        // 친구 요청 보내기
        // 프렌즈의 UUID로 친구 요청 보냄
        @Transactional
        public FriendRequest sendFriendRequest(Caregiver caregiver, UUID friendUUID){

            Optional<Friend> optionalFriend = friendRepository.findById(friendUUID);

            if (optionalFriend.isPresent()){
                Friend friend = optionalFriend.get();

                // 이미 보호자가 등록된 프렌드인 경우, 친구 요청을 보낼 수 없음
                if (friend.getCaregiver() != null){
                    throw new RuntimeException("이미 보호자가 등록된 프렌드입니다.");
                }

                // 이미 친구 요청을 보낸 상태인지 확인
                Optional<FriendRequest> existingRequest = friendRequestRepository.findByCaregiverAndFriendAndStatus(caregiver, friend, "pending");
                if (existingRequest.isPresent()){
                    throw new RuntimeException("이미 프렌즈에게 친구 요청을 보냈습니다.");
                }

                FriendRequest friendRequest = FriendRequest.builder()
                        .friend(friend)
                        .caregiver(caregiver)
                        .build();

                friendRequest.setStatus("pending");

                // FCM 알림 전송
                FcmSendDto fcmSendDto = FcmSendDto.builder()
                        .id(friend.getId())
                        .title("친구 요청")
                        .body("보호자로부터 친구 요청이 도착했습니다.")
                        .build();

//                fcmServiceImpl.sendMessageTo(fcmSendDto);
                try {
                    fcmServiceImpl.sendMessageTo(fcmSendDto);
                } catch (IOException e) {
                    // 예외 처리 로직 (로그 남기기 등)
                    e.printStackTrace();
                    throw new RuntimeException("FCM 메시지 전송에 실패했습니다.", e);
                }

                return friendRequestRepository.save(friendRequest);
            } else{
                throw new RuntimeException("friend not found");
            }
        }


        // 친구 요청 수락하기
        @Transactional
        public void acceptFriendRequest(Long requestId){
            FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                    .orElseThrow(()-> new RuntimeException("Friend Request not found"));

            friendRequest.setStatus("accepted");

            Caregiver caregiver = friendRequest.getCaregiver();
            caregiver.addFriend(friendRequest.getFriend());

            // FCM 알림 전송
            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .id(caregiver.getId())
                    .title("친구 요청 수락")
                    .body("친구 요청이 수락되었습니다.")
                    .build();

            friendRequestRepository.save(friendRequest);
            caregiverRepository.save(caregiver);

        }

        // 친구 요청 거절하기
        @Transactional
        public void rejectFriendRequest(Long requestId){
            FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                    .orElseThrow(()->new RuntimeException("Friend Request not found"));

            friendRequest.setStatus("rejected");
            friendRequestRepository.save(friendRequest);
        }


        // 대기중인 친구 요청 조회하기
        public List<FriendRequest> getPendingRequest(UUID friendId){
            Friend friend = friendRepository.findById(friendId)
                    .orElseThrow(()->new RuntimeException("Friend not found"));

            return friendRequestRepository.findByFriendAndStatus(friend, "pending");
        }
    }