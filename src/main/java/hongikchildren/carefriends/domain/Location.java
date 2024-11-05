package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude; // 위도
    private Double longitude; // 경도
    private LocalDateTime timestamp; // 타임스탬프 (위치 정보 기록 시간)

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="friendId")
    private Friend friend;

    public void setFriend(Friend friend) {
        this.friend = friend;
    }
}
