package sun.board.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.board.like.entity.Likes;
import sun.board.like.entity.TargetType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByTargetIdAndUserId(Long articleId, Long userId);


    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);

    @Modifying
    @Transactional
    @Query("delete from Likes l where l.userId=:userId and l.targetId=:targetId and l.targetType=:targetType")
    int deleteByUserIdAndTargetIdAndTargetType(@Param("userId") Long userId,
                                               @Param("targetId") Long targetId,
                                               @Param("targetType") TargetType targetType);

    @Query("select l.targetId from Likes l " +
            "where l.userId=:userId and l.targetType=:targetType and l.targetId in :targetIds")
    List<Long> findMyLikedTargetIds(@Param("userId") Long userId,
                                    @Param("targetType") TargetType targetType,
                                    @Param("targetIds") List<Long> targetIds);
}
