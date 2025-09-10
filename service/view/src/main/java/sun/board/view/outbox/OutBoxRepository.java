package sun.board.view.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutBoxRepository extends JpaRepository<OutBox,Long> {
}
