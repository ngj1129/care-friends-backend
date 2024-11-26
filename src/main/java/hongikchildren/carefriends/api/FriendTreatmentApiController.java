package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Hospital;
import hongikchildren.carefriends.domain.Task;
import hongikchildren.carefriends.service.FriendService;
import hongikchildren.carefriends.service.HospitalService;
import hongikchildren.carefriends.service.TaskService;
import hongikchildren.carefriends.service.TreatmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treatment")
public class FriendTreatmentApiController {

    private final TreatmentService treatmentService;
    private final FriendService friendService;
    private final HospitalService hospitalService;
    private final TaskService taskService;

    //진료 일정 추가
    @PostMapping
    public TreatmentResponse addTreatment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TreatmentRequest treatmentRequest) {
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



    @Data
    static class HospitalResponse {
        private Long id;
        private String title; //병원 이름
        private String link; //병원 url
        private String address; //병원 주소
        private String telephone; //병원 전화번호

        public HospitalResponse(Long id, String title, String link, String address, String telephone) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.address = address;
            this.telephone = telephone;
        }
    }
    @Data
    static class TreatmentRequest {
        private String title; //병원 이름
        private String link; //병원 url
        private String address; //병원 주소
        private String telephone; //병원 전화번호
        private LocalDate date;
        private LocalTime time;
        private String memo;

        public TreatmentRequest(String title, String link, String address, String telephone, LocalDate date, LocalTime time, String memo) {
            this.title = title;
            this.link = link;
            this.address = address;
            this.telephone = telephone;
            this.date = date;
            this.time = time;
            this.memo = memo;
        }
    }


    @Data
    static class TreatmentResponse {
        private Long id;
        private String title; //병원 이름
        private String link; //병원 url
        private String address; //병원 주소
        private String telephone; //병원 전화번호
        private LocalDate date;
        private LocalTime time;
        private String memo;

        public TreatmentResponse(Long id, String title, String link, String address, String telephone, LocalDate date, LocalTime time, String memo) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.address = address;
            this.telephone = telephone;
            this.date = date;
            this.time = time;
            this.memo = memo;
        }
    }

}
