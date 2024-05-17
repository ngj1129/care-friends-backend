package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendsRepository friendsRepository;

    // Friend 저장
    @Transactional
    public Friend saveFriends(String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        Friend friend = Friend.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birthDate(birthDate)
                .build();
        return friendsRepository.save(friend);
    }

    // 모든 Friend 조회
    public List<Friend> getAllFriends(){
        return friendsRepository.findAll();
    }

    // ID로 Friend 조회
    public Optional<Friend> getFriendsById(Long id){
        return friendsRepository.findById(id);
    }

    // Friends 업데이트
    @Transactional
    public Friend updateFriends(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate){
        Optional<Friend> optionalFriends = friendsRepository.findById(id);
        if (optionalFriends.isPresent()){
            Friend existingFriend = optionalFriends.get(); // get(): optional 객체가 값으로 채워져 있을 때 그 값(엔티티)을 반환
            Friend updatedFriend = Friend.builder()
                    .id(existingFriend.getId()) // 기존 id 유지. save 할 때 동일한 id를 갖는 엔티티가 이미 존재하면 해당 엔티티를 업데이트(merge)함.
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .gender(gender)
                    .birthDate(birthDate)
                    .build();

            // 새로운 객체를 저장하고 반환
            return friendsRepository.save(updatedFriend);
        } else{
            throw new RuntimeException(id + "라는 id의 Friends를 찾을 수 없습니다. ");
        }
    }


    // Friend 삭제
    @Transactional
    public void deleteFriends(Long id){
        friendsRepository.deleteById(id);
    }
}
