package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.FriendRequestRepository;
import hongikchildren.carefriends.repository.LocationRepository;
import hongikchildren.carefriends.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    // Friend 저장
    @Transactional
    public Friend saveFriend(String name, String phoneNumber, String email, Gender gender, LocalDate birthDate, String fcmToken) {
        Friend friend = Friend.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(gender)
                .birthDate(birthDate)
                .fcmToken(fcmToken)
                .build();
        return friendRepository.save(friend);
    }

    // 모든 Friend 조회
    public List<Friend> getAllFriend(){
        return friendRepository.findAll();
    }

    // ID로 Friend 조회
    public Optional<Friend> getFriendById(UUID id){
        return friendRepository.findById(id);
    }

    // email로 Friend 조회
    public Optional<Friend> getFriendByEmail(String email){ return friendRepository.findByEmail(email); }

    // Friend 업데이트
    @Transactional
    public Friend updateFriend(UUID id, String name, String phoneNumber, Gender gender, LocalDate birthDate){
        Optional<Friend> optionalFriend = friendRepository.findById(id);
        if (optionalFriend.isPresent()){
            Friend existingFriend = optionalFriend.get(); // get(): optional 객체가 값으로 채워져 있을 때 그 값(엔티티)을 반환
            Friend updatedFriend = Friend.builder()
                    .id(existingFriend.getId()) // 기존 id 유지. save 할 때 동일한 id를 갖는 엔티티가 이미 존재하면 해당 엔티티를 업데이트(merge)함.
                    .email(existingFriend.getEmail()) // 기존 이메일 유지
                    .fcmToken(existingFriend.getFcmToken()) // 기존 fcmToken 유지
                    .name(name != null ? name : existingFriend.getName()) // 수정할 이름이 없으면 기존 이름 유지
                    .phoneNumber(phoneNumber != null ? phoneNumber : existingFriend.getPhoneNumber()) // 수정할 번호가 없으면 기존 번호 유지
                    .gender(gender != null ? gender : existingFriend.getGender()) // 수정할 성별이 없으면 기존 성별 유지
                    .birthDate(birthDate != null ? birthDate : existingFriend.getBirthDate()) // 수정할 생년월일이 없으면 기존 생년월일 유지
                    .build();

            // 새로운 객체를 저장하고 반환
            return friendRepository.save(updatedFriend);
        } else{
            throw new RuntimeException(id + "라는 id의 Friends를 찾을 수 없습니다. ");
        }
    }
}
