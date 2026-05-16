package hansung.org.terrius.domain.report.repository;

import hansung.org.terrius.domain.report.entity.ReportOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportOwnershipRepository extends JpaRepository<ReportOwnership, Long> {

    boolean existsByUserIdAndReportId(Long userId, Long reportId);

    @Query("""
            select ro.report.id
            from ReportOwnership ro
            where ro.user.id = :userId
              and ro.report.id in :reportIds
            """)
    List<Long> findOwnedReportIds(
            @Param("userId") Long userId,
            @Param("reportIds") List<Long> reportIds
    );

    @Query("""
            select ro
            from ReportOwnership ro
            join fetch ro.report r
            join fetch r.matchVideo mv
            where ro.user.id = :userId
            order by mv.matchDate desc, ro.id desc
            """)
    List<ReportOwnership> findAllByUserIdOrderByLatest(Long userId);

    @Query("""
            select ro
            from ReportOwnership ro
            join fetch ro.report r
            join fetch r.matchVideo mv
            where ro.user.id = :userId
            order by mv.matchDate asc, ro.id asc
            """)
    List<ReportOwnership> findAllByUserIdOrderByOldest(Long userId);
}
