package hansung.org.terrius.domain.stadium.repository;

import hansung.org.terrius.domain.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
}
