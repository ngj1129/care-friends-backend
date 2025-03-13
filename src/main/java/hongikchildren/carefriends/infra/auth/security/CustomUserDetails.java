package hongikchildren.carefriends.infra.auth.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hongikchildren.carefriends.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    private UUID id;

    private String email;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한이 필요하지 않으므로 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        // 비밀번호가 필요 없으므로 null 처리 또는 적절히 처리
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}