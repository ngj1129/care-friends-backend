package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.User;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;

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
}
