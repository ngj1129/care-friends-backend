package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Medicine {

    @Id @GeneratedValue
    @Column(name = "medicineId")
    private Long id;

    private String name;

    //노약자쪽에 식사시간 필드 있어야 될 것 같음.
    private TakeTime takeTime;

    private LocalDate takeStart;

    private LocalDate takeEnd;

    private int numPerDay;

    private String imgUrl;

    private String caution;

    private String effect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendsId")
    private Friends friends;

    @Builder
    public Medicine(Long id, String name, TakeTime takeTime, LocalDate takeStart, LocalDate takeEnd, int numPerDay, String imgUrl, String caution, String effect) {
        this.id = id;
        this.name = name;
        this.takeTime = takeTime;
        this.takeStart = takeStart;
        this.takeEnd = takeEnd;
        this.numPerDay = numPerDay;
        this.imgUrl = imgUrl;
        this.caution = caution;
        this.effect = effect;
    }
}
