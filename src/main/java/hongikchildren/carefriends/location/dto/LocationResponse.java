package hongikchildren.carefriends.location.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

    public LocationResponse(Long id, Double latitude, Double longitude, LocalDateTime timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }
}
