package sun.board.ordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.board.ordering.entity.Ordering;

import java.util.List;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering,Long> {
    
    List<Ordering> findByMemberId(Long memberId);
}
