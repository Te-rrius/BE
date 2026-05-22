package hansung.org.terrius.domain.stadium.repository;

import hansung.org.terrius.domain.stadium.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    Optional<Court> findFirstByStadiumIdOrderByCourtNumberAsc(Long stadiumId);

    boolean existsByStadiumIdAndCourtNumber(Long stadiumId, Integer courtNumber);

    List<Court> findAllByStadiumIdOrderByCourtNumberAsc(Long stadiumId);
}
