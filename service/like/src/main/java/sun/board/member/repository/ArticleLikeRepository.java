package sun.board.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.board.member.entity.Likes;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByTargetIdAndUserId(Long articleId, Long userId);
}
