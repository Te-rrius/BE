package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.HighlightVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighlightVideoRepository extends JpaRepository<HighlightVideo, Long> {
}
