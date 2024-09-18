package hongikchildren.carefriends.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // 조건에 해당되면 메소드 종료
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.substring(7);

        // 토큰 유효성 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return; // 조건에 해당되면 메소드 종료
        }

        // 토큰에서 사용자 정보(이메일) 획득
        String username = jwtUtil.getEmail(token);

        // DB에서 사용자를 조회하여 UserDetails 획득
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
