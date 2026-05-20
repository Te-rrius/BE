package hansung.org.terrius.domain.stadium.web.dto;

import java.util.List;

public record ReportRequestRes(
        List<CalendarDateRes> dates,
        List<MatchVideoRes> times
) {
    public static ReportRequestRes of(List<CalendarDateRes> dates, List<MatchVideoRes> times) {
        return new ReportRequestRes(dates, times);
    }
}
