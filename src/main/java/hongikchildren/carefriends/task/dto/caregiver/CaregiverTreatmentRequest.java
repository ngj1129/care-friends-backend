package hongikchildren.carefriends.task.dto.caregiver;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CaregiverTreatmentRequest {
    private UUID friendId;
    private String title; //병원 이름
    private String link; //병원 url
    private String address; //병원 주소
    private String telephone; //병원 전화번호
    private LocalDate date;
    private LocalTime time;
    private String memo;

    public CaregiverTreatmentRequest(UUID friendId, String title, String link, String address, String telephone, LocalDate date, LocalTime time, String memo) {
        this.friendId = friendId;
        this.title = title;
        this.link = link;
        this.address = address;
        this.telephone = telephone;
        this.date = date;
        this.time = time;
        this.memo = memo;
    }
}
