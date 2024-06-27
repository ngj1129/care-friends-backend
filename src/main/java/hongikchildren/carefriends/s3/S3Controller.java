package hongikchildren.carefriends.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    private final S3Uploader s3Uploader;

    @PostMapping("/api/auth/image")
    public String imageUpload(@RequestPart(required = false) MultipartFile multipartFile)  {
        try {
            if (multipartFile.isEmpty()) {
                return("empty");
            }
            return s3Uploader.uploadFiles(multipartFile, "hongik");
        } catch (Exception e) {
            e.printStackTrace();
            return ("error");
        }
    }
}
