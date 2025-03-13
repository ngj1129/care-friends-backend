package hongikchildren.carefriends.infra.auth.security;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.caregiver.repository.CaregiverRepository;
import hongikchildren.carefriends.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;

    @Cacheable(value = "USER_DETAILS", key = "#email")
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("DB에서 사용자 조회 시도: {}", email);
        Caregiver caregiver = caregiverRepository.findByEmail(email).orElse(null);
        if (caregiver != null) {
            return new CustomUserDetails(caregiver.getId(), caregiver.getEmail());
        }

        Friend friend = friendRepository.findByEmail(email).orElse(null);
        if (friend != null) {
            return new CustomUserDetails(friend.getId(), friend.getEmail());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}
