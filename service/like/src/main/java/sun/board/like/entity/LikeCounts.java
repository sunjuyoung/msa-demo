package sun.board.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "like_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCounts {

    @Id
    private Long targetId;
    private Long likeCount;


    public static LikeCounts init(Long targetId, Long likeCount){
        LikeCounts likeCounts = new LikeCounts();
        likeCounts.targetId = targetId;
        likeCounts.likeCount = likeCount;
        return likeCounts;
    }

    public void increase(){
        this.likeCount++;
    }

    public void decrease(){
        this.likeCount--;
    }
}
