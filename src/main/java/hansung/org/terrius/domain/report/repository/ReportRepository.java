package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByMatchVideoId(Long matchVideoId);
}
