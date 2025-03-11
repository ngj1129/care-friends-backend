package hongikchildren.carefriends.task.api.caregiver;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.caregiver.service.CaregiverService;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.friend.service.FriendService;
import hongikchildren.carefriends.hospital.Hospital;
import hongikchildren.carefriends.hospital.HospitalService;
import hongikchildren.carefriends.hospital.HospitalResponse;
import hongikchildren.carefriends.task.dto.TreatmentResponse;
import hongikchildren.carefriends.task.domain.Task;
import hongikchildren.carefriends.task.dto.caregiver.CaregiverTreatmentRequest;
import hongikchildren.carefriends.task.service.TaskService;
import hongikchildren.carefriends.task.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treatment")
public class CaregiverTreatmentController {

    private final TreatmentService treatmentService;
    private final FriendService friendService;
    private final CaregiverService caregiverService;
    private final HospitalService hospitalService;
    private final TaskService taskService;

    //진료 일정 추가
    @PostMapping("/caregiver")
    public TreatmentResponse addTreatment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CaregiverTreatmentRequest treatmentRequest) {
        String email = userDetails.getUsername();

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

        Friend friend = friendService.getFriendById(treatmentRequest.getFriendId()).orElseThrow();

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

    //진료 일정 삭제


    //프렌드가 방문한 병원 목록
    @GetMapping("/hospital/caregiver/{friendId}")
    public List<HospitalResponse> getHospitals(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID friendId) {
        String email = userDetails.getUsername();

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

        Friend friend = friendService.getFriendById(friendId).orElseThrow();

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
    @GetMapping("/treatment/caregiver/{friendId}")
    public List<TreatmentResponse> getTreatments(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable UUID friendId,
                                                 @RequestParam String hospitalLink) {
        String email = userDetails.getUsername();

        Caregiver caregiver = caregiverService.getCaregiverByEmail(email)
                .orElseThrow(() -> new RuntimeException("보호자를 찾을 수 없습니다."));

        Friend friend = friendService.getFriendById(friendId).orElseThrow();

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
