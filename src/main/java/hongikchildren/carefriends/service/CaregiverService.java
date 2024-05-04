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


// Transactional?
@Service
@Transactional
public class CaregiverService {
    private final CaregiverRepository caregiverRepository;
//    private final FriendsRepository friendsRepository;

    // 밑에 autowired 왜 쓰는지?
    @Autowired
    public CaregiverService(CaregiverRepository caregiverRepository) {
        this.caregiverRepository = caregiverRepository;
    }


    // Caregiver 저장
    public Caregiver saveCaregiver(String name, String phoneNum, Gender gender, LocalDate birthDate){
        Caregiver caregiver = Caregiver.builder()
                .name(name)
                .phoneNumber(phoneNum)
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
    public Optional<Caregiver> getCaregiverById(Long id){
        return caregiverRepository.findById(id);
    }

    // Caregiver 업데이트
    public Caregiver updateCaregiver(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate){
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
    public void deleteCaregiver(Long id){
        caregiverRepository.deleteById(id);
    }

}
