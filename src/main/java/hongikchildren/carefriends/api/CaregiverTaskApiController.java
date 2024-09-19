package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.jwt.JWTUtil;
import hongikchildren.carefriends.service.CaregiverService;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.TaskService;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class CaregiverTaskApiController {

    private final TaskService taskService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    /**
     * 일정 추가
     */
    @PostMapping("/caregiver")
    public ResponseEntity<taskResponse> addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody taskRequest request) {
        // JWT에서 이메일 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtUtil.getEmail(token); // JWT에서 이메일 추출
        System.out.println("JWT에서 추출된 이메일: " + email);

        // 이메일로 보호자 정보 조회
        Caregiver caregiver = caregiverService.getCaregiverByEmail(email).orElseThrow(() -> new IllegalArgumentException("보호자를 찾을 수 없습니다."));

        // id로 친구 정보 조회
        Friend friend = friendService.getFriendById(request.getFriendId()).orElseThrow();

        // Task 생성 및 저장
        Task task = taskService.saveTask(
                friend,
                request.getDate(),
                request.getStartTime(),
                request.getTitle(),
                request.getLocation(),
                request.getMemo(),
                request.getPeriodType(),
                request.getPeriod()
        );

        return new ResponseEntity<>(new taskResponse(task.getGroupId()), HttpStatus.OK);
    }


    @Data
    static class taskRequest {
        private UUID friendId;
        private LocalDate date;
        private PeriodType periodType;
        private int period;
        private LocalTime startTime;
        //private LocalTime signalTime; 일단 기본 10분 전 알람
        private String title;
        private String location;
        private String memo;

        public taskRequest(UUID friendId, LocalDate date, PeriodType periodType, int period, LocalTime startTime, String title, String location, String memo) {
            this.friendId = friendId;
            this.date = date;
            this.periodType = periodType;
            this.period = period;
            this.startTime = startTime;
            this.title = title;
            this.location = location;
            this.memo = memo;
        }
    }

    @Data
    static class taskResponse {
        private long id; //이 id는 그룹 id이다.

        public taskResponse(long id) {
            this.id = id;
        }
    }

}
