package sun.board.member.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sun.board.member.entity.LikeCounts;

import java.util.Optional;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<LikeCounts, Long> {

    //select .. for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LikeCounts> findLockedByTargetId(Long articleId);


    @Query(
            value = "update like_counts set like_count = like_count + 1 where target_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increaseLikeCount(@Param("articleId") Long articleId);

    @Query(
            value = "update like_counts set like_count = like_count -1 where target_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decreaseLikeCount(@Param("articleId") Long articleId);
}
