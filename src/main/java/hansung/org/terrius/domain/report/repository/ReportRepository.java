package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByMatchVideoId(Long matchVideoId);

    @Query("""
            select distinct r
            from Report r
            join fetch r.matchVideo mv
            left join fetch r.highlightVideos hv
            where mv.id = :matchVideoId
              and r.target = :target
            """)
    Optional<Report> findDetailByMatchVideoIdAndTarget(Long matchVideoId, ReportTarget target);
}
