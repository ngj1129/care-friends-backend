package hongikchildren.carefriends.location.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationUpdateRequest {
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

    public LocationUpdateRequest(Double latitude, Double longitude, LocalDateTime timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }
}
