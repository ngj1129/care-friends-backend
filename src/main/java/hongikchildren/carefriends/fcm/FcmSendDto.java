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

    @Builder
    public FcmSendDto(UUID id, String title, String body) {
        //this.token = token;
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
