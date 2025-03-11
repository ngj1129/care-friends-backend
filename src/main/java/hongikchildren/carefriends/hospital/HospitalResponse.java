package hongikchildren.carefriends.hospital;

import lombok.Data;

@Data
public class HospitalResponse {
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
