package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.User;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;
    private final HospitalRepository hospitalRepository;

    public String isExistingUser(String email) {
        if (caregiverRepository.findByEmail(email).isPresent()) {
            return "caregiver";
        }
        if (friendRepository.findByEmail(email).isPresent()) {
            return "friend";
        }
        return "null";
    }

    public User getUserByEmail(String email) {
        if (caregiverRepository.findByEmail(email).isPresent()) {
            return caregiverRepository.findByEmail(email).get();
        }
        if (friendRepository.findByEmail(email).isPresent()) {
            return friendRepository.findByEmail(email).get();
        }
        return null;
    }

    @Transactional
    public User setProfile(String email, String profileImg) {
        User user;
        if (caregiverRepository.findByEmail(email).isPresent()) {
            user = caregiverRepository.findByEmail(email).get();
            user.setProfileImg(profileImg);
            return user;
        }
        if (friendRepository.findByEmail(email).isPresent()) {
            user = friendRepository.findByEmail(email).get();
            user.setProfileImg(profileImg);
            return user;
        }
        return null;
    }

    /*
    회원 탈퇴
     */
    @Transactional
    public void unregister(User user) {
        if (user instanceof Caregiver caregiver) {

            // JPQL을 이용해 caregiver와 연관된 모든 Friend의 caregiver를 null로 설정
//            friendRepository.removeCaregiverFromFriends(caregiver.getId());

            caregiver.getFriends().forEach(friend -> friend.setCaregiver(null)); // caregiver와의 관계 해제

            // Caregiver 삭제
            caregiverRepository.deleteById(caregiver.getId());

        } else if (user instanceof Friend friend) {
            // 1. 연관된 Hospital 데이터 삭제
//            hospitalRepository.deleteByFriendId(friend.getId());

            // Friend 삭제
            friendRepository.deleteById(friend.getId());

        } else {
            throw new IllegalArgumentException("알 수 없는 사용자 유형입니다.");
        }
    }
}
