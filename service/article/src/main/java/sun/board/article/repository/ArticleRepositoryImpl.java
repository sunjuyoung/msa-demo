package sun.board.article.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import sun.board.article.entity.Article;
import sun.board.article.service.response.ArticleResponse;

import java.util.List;


//public class ArticleRepositoryImpl implements CustomArticleRepository{

//    private final JPAQueryFactory queryFactory;
//
//    public ArticleRepositoryImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Override
//    public List<Article> readAllPage(Long boardId, Pageable pageable) {
//
//        List<Long> articleIds = queryFactory
//                .select(article.articleId)
//                .from(article)
//                .where(article.boardId.eq(boardId))
//                .orderBy(article.articleId.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return queryFactory
//                .selectFrom(article)
//                .where(article.articleId.in(articleIds))
//                .orderBy(article.articleId.desc())
//                .fetch();
//
//    }
//
//    @Override
//    public Long countQuerydsl(Long boardId, Long limit) {
//
//        // ✅ count 쿼리 최적화
//        return queryFactory
//                .select(article.count())
//                .from(article)
//                .where(article.boardId.eq(boardId))
//                .fetchOne();
//
//    }
//
//    @Override
//    public Page<Article> findAllTest(Long boardId, Pageable pageable) {
//
//        List<Article> articles = queryFactory
//                .selectFrom(article)
//                .where(article.boardId.eq(boardId))
//                .orderBy(article.articleId.desc()) // ✅ 인덱스를 활용한 정렬
//                .offset(pageable.getOffset()) // ✅ 수동으로 offset 적용
//                .limit(pageable.getPageSize()) // ✅ 수동으로 limit 적용
//                .fetch();
//
//        // 전체 개수 조회
//        long total = queryFactory
//                .select(article.count())
//                .from(article)
//                .where(article.boardId.eq(boardId))
//                .fetchOne();
//
//        return new PageImpl<>(articles, pageable, total);
//    }
//}
