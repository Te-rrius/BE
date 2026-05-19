package hansung.org.terrius.domain.stadium.repository;

import hansung.org.terrius.domain.stadium.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
}
