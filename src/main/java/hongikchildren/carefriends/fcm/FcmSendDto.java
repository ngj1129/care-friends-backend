package hongikchildren.carefriends.fcm;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDto {
   // private String token;
    private UUID id;
    private String title;
    private String body;
    private String receiverType; // caregiver, friend로 구분
    private String type;
    private String data;

    @Builder
    public FcmSendDto(UUID id, String title, String body, String receiverType, String type, String data) {
        //this.token = token;
        this.id = id;
        this.title = title;
        this.body = body;
        this.receiverType = receiverType;
        this.type = type;
        this.data = data;
    }
}
