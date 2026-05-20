package hansung.org.terrius.domain.stadium.util;

import hansung.org.terrius.domain.stadium.exception.CourtErrorCode;
import hansung.org.terrius.domain.stadium.exception.CourtException;
import hansung.org.terrius.domain.stadium.exception.StadiumErrorCode;
import hansung.org.terrius.domain.stadium.exception.StadiumException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validator {

    public static final int DOWNLOAD_DATE_WINDOW_SIZE = 8;
    public static final int REQUEST_DATE_WINDOW_SIZE = 3;

    public static LocalDate getWindowStart(LocalDate today, int windowSize) {
        return today.minusDays(windowSize - 1);
    }

    public static void validateCourtNumber(Integer courtNumber) {
        if (courtNumber == null) {
            return;
        }
        if (courtNumber <= 0) {
            throw new CourtException(CourtErrorCode.COURT_INVALID_NUMBER);
        }
    }

    public static void validateDateInWindow(LocalDate date, LocalDate today, int windowSize) {
        if (date == null) {
            return;
        }
        LocalDate windowStart = getWindowStart(today, windowSize);
        if (date.isBefore(windowStart) || date.isAfter(today)) {
            throw new StadiumException(StadiumErrorCode.STADIUM_INVALID_DATE);
        }
    }
}
