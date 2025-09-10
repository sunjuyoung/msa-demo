package sun.board.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sun.board.view.entity.TargetType;
import sun.board.view.entity.ViewCount;

@Repository
public interface ViewCountBackUpRepository extends JpaRepository<ViewCount, Long> {



    @Query(
            value = "update view_count set view_counts = :viewCount" +
                    "  where target_id = :articleId and target_type = :targetType and view_counts < :viewCount",
            nativeQuery = true
    )
    @Modifying
    int updateArticleViewCount(
            @Param("articleId") Long articleId,
            @Param("targetType") TargetType targetType,
            @Param("viewCount") Long viewCount
    );

    @Query(
            value = "update view_count set view_counts = view_counts + :viewCount" +
                    "  where target_id = :productOptionId and target_type = :targetType ",
            nativeQuery = true
    )
    @Modifying
    int updateProductViewCount(
            @Param("productOptionId") Long productOptionId,
            @Param("targetType") String targetType,
            @Param("viewCount") Long viewCount
    );
}
