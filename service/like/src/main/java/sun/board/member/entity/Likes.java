package sun.board.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "likes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Likes {

    @Id
    private Long likeId;
    private Long targetId;
    @Enumerated(EnumType.STRING)
    private TargetType targetType;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Likes create(Long articleLikeId, Long targetId, Long userId, TargetType targetType) {
        Likes likes = new Likes();
        likes.likeId = articleLikeId;
        likes.targetId = targetId;
        likes.userId = userId;
        likes.targetType = targetType;
        likes.createdAt = LocalDateTime.now();
        likes.updatedAt = LocalDateTime.now();
        return likes;
    }
}
