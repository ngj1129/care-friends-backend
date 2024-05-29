package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FriendRequest {
    @Id @GeneratedValue
    @Column(name = "friendsRequestId")
    private Long id;


    @ManyToOne
    @JoinColumn(name="caregiverId")
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name="friendId")
    private Friend friend;

    private String status; // pending, accepted, rejected

    @Builder
    public FriendRequest(Long id, Caregiver caregiver, Friend friend, String status) {
        this.id = id;
        this.caregiver = caregiver;
        this.friend = friend;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
