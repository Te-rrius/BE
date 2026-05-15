package hansung.org.terrius.domain.match.repository;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchVideoRepository extends JpaRepository<MatchVideo, Long> {
}
