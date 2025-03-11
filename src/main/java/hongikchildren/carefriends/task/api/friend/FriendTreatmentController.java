package hongikchildren.carefriends.task.api.friend;

import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.hospital.Hospital;
import hongikchildren.carefriends.hospital.HospitalService;
import hongikchildren.carefriends.hospital.HospitalResponse;
import hongikchildren.carefriends.task.dto.TreatmentResponse;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.dto.friend.FriendTreatmentRequest;
import hongikchildren.carefriends.task.service.TaskService;
import hongikchildren.carefriends.task.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treatment")
public class FriendTreatmentController {

    private final TreatmentService treatmentService;
    private final FriendService friendService;
    private final HospitalService hospitalService;
    private final TaskService taskService;

    //진료 일정 추가
    @PostMapping
    public TreatmentResponse addTreatment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FriendTreatmentRequest treatmentRequest) {
        String email = userDetails.getUsername();

        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌드를 찾을 수 없습니다."));

        System.out.println(treatmentRequest.getLink());
        if (treatmentRequest.getLink().length() > 1000) {
            throw new IllegalArgumentException("URL length exceeds the maximum allowed size");
        }

        // 병원 조회 또는 새 병원 생성
        Hospital hospital = hospitalService.getHospitalByLinkOrCreate(
                treatmentRequest.getLink(),
                treatmentRequest.getTitle(),
                treatmentRequest.getAddress(),
                treatmentRequest.getTelephone(),
                friend
        );

        String treatmentTitle = treatmentRequest.getTitle() + " 방문";
        Task treatment = treatmentService.saveTreatment(friend, hospital, treatmentRequest.getDate(),
                treatmentRequest.getTime(), treatmentTitle, treatmentRequest.getAddress(), treatmentRequest.getMemo());

        return new TreatmentResponse(
                treatment.getId(),
                treatment.getHospital().getName(),
                treatment.getHospital().getLink(),
                treatment.getHospital().getAddress(),
                treatment.getHospital().getPhone(),
                treatment.getDate(),
                treatment.getStartTime(),
                treatment.getMemo()
        );
    }
    //진료 일정 수정

    //진료 상태 변경
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId) {
        taskService.setTaskStatusToDone(taskId);
        return ResponseEntity.noContent().build();
    }

    //병원 삭제
    @DeleteMapping("/hospital")
    public void deleteHospital(@RequestBody Map<String, Long> payload) {
        System.out.println(payload.get("id"));
        hospitalService.deleteHospital(payload.get("id"));
    }

    //프렌드가 방문한 병원 목록
    @GetMapping("/hospital")
    public List<HospitalResponse> getHospitals(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌드를 찾을 수 없습니다."));

        // Friend가 방문한 병원 목록 가져오기
        List<Hospital> hospitals = hospitalService.getAllHospitals(friend);

        // Hospital 엔티티 목록을 HospitalResponse 목록으로 변환
        return hospitals.stream()
                .map(hospital -> new HospitalResponse(
                        hospital.getId(),
                        hospital.getName(),
                        hospital.getLink(),
                        hospital.getAddress(),
                        hospital.getPhone()
                ))
                .collect(Collectors.toList());


    }

    //병원 별 진료 목록
    @GetMapping("/treatment")
    public List<TreatmentResponse> getTreatments(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String hospitalLink) {
        String email = userDetails.getUsername();

        Friend friend = friendService.getFriendByEmail(email)
                .orElseThrow(() -> new RuntimeException("프렌드를 찾을 수 없습니다."));

        Hospital hospital = hospitalService.getHospitalByLink(hospitalLink);

        System.out.println(hospital.getAddress());

        List<Task> treatments = treatmentService.getTreatmentsByHospitalAndFriend(hospital, friend);

        return treatments.stream()
                .map(treatment -> new TreatmentResponse(
                        treatment.getId(),
                        treatment.getTitle(),
                        treatment.getHospital().getLink(),
                        treatment.getHospital().getAddress(),
                        treatment.getHospital().getPhone(),
                        treatment.getDate(),
                        treatment.getStartTime(),
                        treatment.getMemo()
                ))
                .collect(Collectors.toList());

    }
}
