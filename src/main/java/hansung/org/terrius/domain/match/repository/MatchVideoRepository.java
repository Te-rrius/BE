package hansung.org.terrius.domain.match.repository;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchVideoRepository extends JpaRepository<MatchVideo, Long> {

    @Query("""
            select distinct mv.matchDate
            from MatchVideo mv
            where mv.court.stadium.id = :stadiumId
              and mv.matchDate between :start and :end
              and mv.reportRequested = true
            """)
    List<LocalDate> findReportedMatchDates(
            @Param("stadiumId") Long stadiumId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            select mv
            from MatchVideo mv
            where mv.court.stadium.id = :stadiumId
              and mv.court.courtNumber = :courtNumber
              and mv.matchDate = :date
              and mv.reportRequested = true
            order by mv.startTime asc
            """)
    List<MatchVideo> findReportedMatchVideos(
            @Param("stadiumId") Long stadiumId,
            @Param("courtNumber") Integer courtNumber,
            @Param("date") LocalDate date
    );

    Optional<MatchVideo> findByIdAndCourt_Stadium_Id(Long id, Long stadiumId);

    @Query("""
            select mv
            from MatchVideo mv
            where mv.court.stadium.id = :stadiumId
              and mv.court.courtNumber = :courtNumber
              and mv.matchDate = :date
            order by mv.startTime asc
            """)
    List<MatchVideo> findAllByStadiumAndCourtAndDate(
            @Param("stadiumId") Long stadiumId,
            @Param("courtNumber") Integer courtNumber,
            @Param("date") LocalDate date
    );
}
