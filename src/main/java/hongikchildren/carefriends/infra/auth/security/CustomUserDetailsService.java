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

    @Cacheable(value = "USER_DETAILS", key = "#email", cacheManager = "cacheManager")
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("DB에서 사용자 조회 시도: {}", email);
        Caregiver caregiver = caregiverRepository.findByEmail(email).orElse(null);
        if (caregiver != null) {
            return new CustomUserDetails(caregiver);
        }

        Friend friend = friendRepository.findByEmail(email).orElse(null);
        if (friend != null) {
            return new CustomUserDetails(friend);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}

//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final CaregiverRepository caregiverRepository;
//    private final FriendRepository friendRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        // 먼저 Caregiver에서 조회
//        Caregiver caregiver = caregiverRepository.findByEmail(email).orElse(null);
//        if (caregiver != null) {
//            return new CustomUserDetails(caregiver);
//        }
//
//        // Caregiver에 없으면 Friend에서 조회
//        Friend friend = friendRepository.findByEmail(email).orElse(null);
//        if (friend != null) {
//            return new CustomUserDetails(friend);
//        }
//
//        // Caregiver나 Friend 모두 없으면 예외 처리
//        throw new UsernameNotFoundException("User not found with email: " + email);
//    }
//}
