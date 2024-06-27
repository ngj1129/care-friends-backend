package hongikchildren.carefriends.api;

import hongikchildren.carefriends.domain.Medicine;
import hongikchildren.carefriends.domain.TakeTime;
import hongikchildren.carefriends.s3.S3Uploader;
import hongikchildren.carefriends.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
public class MedicineApiController {
    private final MedicineService medicineService;
    private final S3Uploader s3Uploader;

    @PostMapping("/medicine")
    public Medicine saveMedicine(@RequestPart(required = false) MultipartFile image) {
        String getImageUrl = "null";
        try {
            getImageUrl = s3Uploader.uploadFiles(image, "medicine");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Medicine medicine = medicineService.saveMedicine("약", TakeTime.AFTER, LocalDate.now(), LocalDate.now(), 3, getImageUrl, "...", "effect");
        return medicine;
    }
}
