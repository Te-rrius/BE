package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.ReportMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportMaterialRepository extends JpaRepository<ReportMaterial, Long> {
}
