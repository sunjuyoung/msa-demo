package sun.board.view.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_count")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private Long viewCounts;

    public static ViewCount create(Long targetId, Long viewCount, TargetType targetType) {
        ViewCount articleViewCount = new ViewCount();
        articleViewCount.targetId = targetId;
        articleViewCount.targetType = targetType;
        articleViewCount.viewCounts = viewCount;

        return articleViewCount;
    }



}
