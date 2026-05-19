package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;
import hansung.org.terrius.domain.stadium.web.dto.MatchVideoRes;

import java.time.LocalDate;
import java.util.List;

public interface StadiumService {

    List<StadiumRes> getStadiums(String province, String city, String name);

    List<CalendarDateRes> getReportDownloadDates(Long stadiumId);

    List<MatchVideoRes> getReportDownloadTimes(Long stadiumId, LocalDate date, Integer courtNumber);
}
