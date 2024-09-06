package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
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
public class CaregiverService {
    private final CaregiverRepository caregiverRepository;


    // Caregiver 저장
    @Transactional
    public Caregiver saveCaregiver(String name, String phoneNum, String email, Gender gender, LocalDate birthDate){
        Caregiver caregiver = Caregiver.builder()
                .name(name)
                .phoneNumber(phoneNum)
                .email(email)
                .gender(gender)
                .birthDate(birthDate)
                .build();
        return caregiverRepository.save(caregiver);

    }


    // 모든 Caregiver 조회
    public List<Caregiver> getAllCaregivers(){
        return caregiverRepository.findAll();
    }

    // ID로 Caregiver 조회
    public Optional<Caregiver> getCaregiverById(UUID id){
        return caregiverRepository.findById(id);
    }

    // Caregiver 업데이트
    @Transactional
    public Caregiver updateCaregiver(UUID id, String name, String phoneNumber, Gender gender, LocalDate birthDate){
        Optional<Caregiver> optionalCaregiver = caregiverRepository.findById(id);
        if (optionalCaregiver.isPresent()){
            Caregiver existingCaregiver = optionalCaregiver.get(); // get(): optional 객체가 값으로 채워져 있을 때 그 값(엔티티)을 반환
            Caregiver updatedCaregiver = Caregiver.builder()
                    .id(existingCaregiver.getId()) // 기존 id 유지. save 할 때 동일한 id를 갖는 엔티티가 이미 존재하면 해당 엔티티를 업데이트(merge)함.
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .gender(gender)
                    .birthDate(birthDate)
                    .build();

            // 새로운 객체를 저장하고 반환
            return caregiverRepository.save(updatedCaregiver);
        } else{
            throw new RuntimeException(id + "라는 id의 Caregiver를 찾을 수 없습니다. ");
        }
    }


    // Caregiver 삭제
    @Transactional
    public void deleteCaregiver(UUID id){
        caregiverRepository.deleteById(id);
    }

    // Caregiver가 관리하는 friends 등록
    @Transactional
    public void addFriendToCaregiver(UUID caregiverId, Friend friend){
        Optional<Caregiver> optionalCaregiver = caregiverRepository.findById(caregiverId);
        if (optionalCaregiver.isPresent()){
            Caregiver caregiver = optionalCaregiver.get();
            if(friend.getCaregiver() != null && !friend.getCaregiver().equals(caregiver)){ // 이미 friends를 다른 caregiver가 등록한 경우
                throw new RuntimeException("이미 caregiver가 등록된 friends입니다.");
            }

            caregiver.addFriend(friend); // caregiver에 friends 추가
            caregiverRepository.save(caregiver); // 변경된 caregiver 저장
        } else{
            throw new RuntimeException(caregiverId + "를 찾을 수 없음");
        }
    }

    // Caregiver의 friends 삭제
    @Transactional
    public void deleteFriendFromCaregiver(UUID caregiverId, Friend friend){
        Optional<Caregiver> optionalCaregiver = caregiverRepository.findById(caregiverId);
        if (optionalCaregiver.isPresent()) {
            Caregiver caregiver = optionalCaregiver.get();
            caregiver.removeFriend(friend);
            caregiverRepository.save(caregiver);
        } else{
            throw new RuntimeException(caregiverId + "를 찾을 수 없음");
        }
    }


}
