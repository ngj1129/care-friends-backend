package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friends;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendsService {

    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }



    // Friend 저장
    @Transactional
    public Friends saveFriends(String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        Friends friends = Friends.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birthDate(birthDate)
                .build();
        return friendsRepository.save(friends);
    }

    // 모든 Friend 조회
    public List<Friends> getAllFriends(){
        return friendsRepository.findAll();
    }

    // ID로 Friend 조회
    public Optional<Friends> getFriendsById(Long id){
        return friendsRepository.findById(id);
    }

    // Friends 업데이트
    public Friends updateFriends(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate){
        Optional<Friends> optionalFriends = friendsRepository.findById(id);
        if (optionalFriends.isPresent()){
            Friends existingFriends = optionalFriends.get(); // get(): optional 객체가 값으로 채워져 있을 때 그 값(엔티티)을 반환
            Friends updatedFriends = Friends.builder()
                    .id(existingFriends.getId()) // 기존 id 유지. save 할 때 동일한 id를 갖는 엔티티가 이미 존재하면 해당 엔티티를 업데이트(merge)함.
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .gender(gender)
                    .birthDate(birthDate)
                    .build();

            // 새로운 객체를 저장하고 반환
            return friendsRepository.save(updatedFriends);
        } else{
            throw new RuntimeException(id + "라는 id의 Friends를 찾을 수 없습니다. ");
        }
    }


    // Friend 삭제
    public void deleteFriends(Long id){
        friendsRepository.deleteById(id);
    }


    // friends의 caregiver 등록하기
    public void registerCaregiver(Long friendsId, Caregiver caregiver){
        Optional<Friends> optionalFriends = friendsRepository.findById(friendsId);
        if (optionalFriends.isPresent()){
            Friends friends = optionalFriends.get();
            friends.setCaregiver(caregiver); // caregiver 설정
            friendsRepository.save(friends); // 엔티티 업데이트
        } else{
            throw new RuntimeException(friendsId + "를 찾을 수 없음");
        }
    }

}
