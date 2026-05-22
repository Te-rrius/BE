package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.MatchVideoRes;
import hansung.org.terrius.domain.stadium.web.dto.ReportRequestRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumDetailRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;

import java.time.LocalDate;
import java.util.List;

public interface StadiumService {

    List<StadiumRes> getStadiums(String province, String city, String name);

    StadiumDetailRes getStadiumCourts(Long stadiumId);

    List<CalendarDateRes> getReportDownloadDates(Long stadiumId);

    List<MatchVideoRes> getReportDownloadTimes(Long stadiumId, LocalDate date, Integer courtNumber);

    ReportRequestRes getReportRequests(Long stadiumId, LocalDate date, Integer courtNumber);

    void requestReport(Long stadiumId, Long matchVideoId);
}
