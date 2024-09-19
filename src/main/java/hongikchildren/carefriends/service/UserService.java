package hongikchildren.carefriends.service;


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
}
