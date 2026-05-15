package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.ReportOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOwnershipRepository extends JpaRepository<ReportOwnership, Long> {

    boolean existsByUserIdAndReportId(Long userId, Long reportId);
}
