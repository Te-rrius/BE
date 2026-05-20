package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.match.exception.MatchErrorCode;
import hansung.org.terrius.domain.match.exception.MatchException;
import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.stadium.entity.Court;
import hansung.org.terrius.domain.stadium.entity.Stadium;
import hansung.org.terrius.domain.stadium.exception.CourtErrorCode;
import hansung.org.terrius.domain.stadium.exception.CourtException;
import hansung.org.terrius.domain.stadium.exception.StadiumErrorCode;
import hansung.org.terrius.domain.stadium.exception.StadiumException;
import hansung.org.terrius.domain.stadium.repository.CourtRepository;
import hansung.org.terrius.domain.stadium.repository.StadiumRepository;
import hansung.org.terrius.domain.stadium.util.Validator;
import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.MatchVideoRes;
import hansung.org.terrius.domain.stadium.web.dto.ReportRequestRes;
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

    private final StadiumRepository stadiumRepository;
    private final CourtRepository courtRepository;
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
        LocalDate start = Validator.getWindowStart(today, Validator.DOWNLOAD_DATE_WINDOW_SIZE);

        Set<LocalDate> reportedDates = new HashSet<>(
                matchVideoRepository.findReportedMatchDates(stadiumId, start, today)
        );

        return IntStream.range(0, Validator.DOWNLOAD_DATE_WINDOW_SIZE)
                .mapToObj(start::plusDays)
                .map(date -> CalendarDateRes.of(date, reportedDates.contains(date)))
                .toList();
    }

    @Override
    public List<MatchVideoRes> getReportDownloadTimes(Long stadiumId, LocalDate date, Integer courtNumber) {
        LocalDate today = LocalDate.now();

        if (!stadiumRepository.existsById(stadiumId)) {
            throw new StadiumException(StadiumErrorCode.STADIUM_NOT_FOUND);
        }

        Validator.validateDateInWindow(date, today, Validator.DOWNLOAD_DATE_WINDOW_SIZE);
        Validator.validateCourtNumber(courtNumber);

        if (courtNumber != null && !courtRepository.existsByStadiumIdAndCourtNumber(stadiumId, courtNumber)) {
            throw new CourtException(CourtErrorCode.COURT_NOT_FOUND);
        }

        // 디폴트 값
        LocalDate targetDate = (date != null) ? date : today;
        Integer targetCourtNumber = (courtNumber != null)
                ? courtNumber
                : courtRepository.findFirstByStadiumIdOrderByCourtNumberAsc(stadiumId)
                        .map(Court::getCourtNumber)
                        .orElseThrow(() -> new CourtException(CourtErrorCode.COURT_NOT_FOUND)); // 구장 안에 코트 자체가 존재 X

        return matchVideoRepository.findReportedMatchVideos(stadiumId, targetCourtNumber, targetDate)
                .stream()
                .map(MatchVideoRes::from)
                .toList();
    }

    @Override
    public ReportRequestRes getReportRequests(Long stadiumId, LocalDate date, Integer courtNumber) {
        LocalDate today = LocalDate.now();

        if (!stadiumRepository.existsById(stadiumId)) {
            throw new StadiumException(StadiumErrorCode.STADIUM_NOT_FOUND);
        }

        Validator.validateDateInWindow(date, today, Validator.REQUEST_DATE_WINDOW_SIZE);
        Validator.validateCourtNumber(courtNumber);

        if (courtNumber != null && !courtRepository.existsByStadiumIdAndCourtNumber(stadiumId, courtNumber)) {
            throw new CourtException(CourtErrorCode.COURT_NOT_FOUND);
        }

        // 디폴트 값
        LocalDate targetDate = (date != null) ? date : today;
        Integer targetCourtNumber = (courtNumber != null)
                ? courtNumber
                : courtRepository.findFirstByStadiumIdOrderByCourtNumberAsc(stadiumId)
                        .map(Court::getCourtNumber)
                        .orElseThrow(() -> new CourtException(CourtErrorCode.COURT_NOT_FOUND));

        LocalDate start = Validator.getWindowStart(today, Validator.REQUEST_DATE_WINDOW_SIZE);
        Set<LocalDate> reportedDates = new HashSet<>(
                matchVideoRepository.findReportedMatchDates(stadiumId, start, today)
        );
        List<CalendarDateRes> dates = IntStream.range(0, Validator.REQUEST_DATE_WINDOW_SIZE)
                .mapToObj(start::plusDays)
                .map(d -> CalendarDateRes.of(d, reportedDates.contains(d)))
                .toList();

        List<MatchVideoRes> times = matchVideoRepository
                .findAllByStadiumAndCourtAndDate(stadiumId, targetCourtNumber, targetDate)
                .stream()
                .map(MatchVideoRes::from)
                .toList();

        return ReportRequestRes.of(dates, times);
    }

    @Override
    @Transactional
    public void requestReport(Long stadiumId, Long matchVideoId) {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new StadiumException(StadiumErrorCode.STADIUM_NOT_FOUND);
        }

        if (!matchVideoRepository.existsById(matchVideoId)) {
            throw new MatchException(MatchErrorCode.MATCH_VIDEO_NOT_FOUND);
        }

        MatchVideo matchVideo = matchVideoRepository.findByIdAndCourt_Stadium_Id(matchVideoId, stadiumId)
                .orElseThrow(() -> new MatchException(MatchErrorCode.MATCH_VIDEO_STADIUM_MISMATCH));

        if (Boolean.TRUE.equals(matchVideo.getReportRequested())) {
            throw new MatchException(MatchErrorCode.MATCH_VIDEO_ALREADY_REQUESTED);
        }

        matchVideo.requestReport();
    }


    // -------------------- 메서드 --------------------
    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
