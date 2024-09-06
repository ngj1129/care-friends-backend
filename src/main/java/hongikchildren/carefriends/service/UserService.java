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

    public boolean isExistingUser(String email) {
        if (caregiverRepository.findByEmail(email).isPresent()) {
            return true;
        }
        return friendRepository.findByEmail(email).isPresent();
    }
}
