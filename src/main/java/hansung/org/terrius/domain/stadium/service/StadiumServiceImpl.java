package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.stadium.entity.Stadium;
import hansung.org.terrius.domain.stadium.exception.StadiumErrorCode;
import hansung.org.terrius.domain.stadium.exception.StadiumException;
import hansung.org.terrius.domain.stadium.repository.StadiumRepository;
import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StadiumServiceImpl implements StadiumService {

    private static final int DATE_WINDOW_SIZE = 8;

    private final StadiumRepository stadiumRepository;
    private final MatchVideoRepository matchVideoRepository;

    @Override
    public List<StadiumRes> getStadiums(String province, String city, String name) {
        List<Stadium> stadiums = stadiumRepository.findAllBySearchCondition(
                normalize(province),
                normalize(city),
                normalize(name)
        );

        return stadiums.stream()
                .map(StadiumRes::from)
                .toList();
    }

    @Override
    public List<CalendarDateRes> getReportDownloadDates(Long stadiumId) {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new StadiumException(StadiumErrorCode.STADIUM_NOT_FOUND);
        }

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(DATE_WINDOW_SIZE - 1);

        Set<LocalDate> reportedDates = new HashSet<>(
                matchVideoRepository.findReportedMatchDates(stadiumId, start, today)
        );

        return IntStream.range(0, DATE_WINDOW_SIZE)
                .mapToObj(start::plusDays)
                .map(date -> CalendarDateRes.of(date, reportedDates.contains(date)))
                .toList();
    }


    // -------------------- 메서드 --------------------
    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
