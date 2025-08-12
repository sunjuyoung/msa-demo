package sun.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sun.board.comment.entity.ArticleCommentCount;

@Repository
public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount, Long> {


    @Query(
            value = "update article_comment_count set comment_count = comment_count + 1 where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increaseCommentCount(@Param("articleId") Long articleId);

    @Query(
            value = "update article_comment_count set comment_count = comment_count -1 where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decreaseCommentCount(@Param("articleId") Long articleId);

}
