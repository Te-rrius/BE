package hansung.org.terrius.domain.stadium.web.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record CalendarDateRes(
        LocalDate date,
        DayOfWeek dayOfWeek,
        boolean hasReport
) {
    public static CalendarDateRes of(LocalDate date, boolean hasReport) {
        return new CalendarDateRes(
                date,
                date.getDayOfWeek(),
                hasReport
        );
    }
}
