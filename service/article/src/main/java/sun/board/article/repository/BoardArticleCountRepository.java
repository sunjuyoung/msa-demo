package sun.board.article.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sun.board.article.entity.BoardArticleCount;

import java.util.Optional;

@Repository
public interface BoardArticleCountRepository extends JpaRepository<BoardArticleCount, Long> {



    @Query(
            value = "update board_article_count set article_count = article_count + 1 where board_id = :boardId",
            nativeQuery = true
    )
    @Modifying
    int increaseArticleCount(@Param("boardId") Long boardId);

    @Query(
            value = "update board_article_count set article_count = article_count -1 where board_id = :boardId",
            nativeQuery = true
    )
    @Modifying
    int decreaseArticleCount(@Param("boardId") Long boardId);
}
