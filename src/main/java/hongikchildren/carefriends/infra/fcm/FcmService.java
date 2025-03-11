package hongikchildren.carefriends.infra.fcm;

import hongikchildren.carefriends.infra.fcm.dto.FcmSendDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FcmService {
    int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;
}
