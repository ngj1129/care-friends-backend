package hongikchildren.carefriends.jwt;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 먼저 Caregiver에서 조회
        Caregiver caregiver = caregiverRepository.findByEmail(email).orElse(null);
        if (caregiver != null) {
            return new CustomUserDetails(caregiver);
        }

        // Caregiver에 없으면 Friend에서 조회
        Friend friend = friendRepository.findByEmail(email).orElse(null);
        if (friend != null) {
            return new CustomUserDetails(friend);
        }

        // Caregiver나 Friend 모두 없으면 예외 처리
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
