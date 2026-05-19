package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;

import java.util.List;

public interface StadiumService {

    List<StadiumRes> getStadiums(String province, String city, String name);

    List<CalendarDateRes> getReportDownloadDates(Long stadiumId);
}
