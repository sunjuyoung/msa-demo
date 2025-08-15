package sun.board.ordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.board.ordering.entity.Ordering;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering,Long> {
}
