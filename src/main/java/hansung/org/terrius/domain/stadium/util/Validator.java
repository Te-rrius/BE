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

    public static final int DATE_WINDOW_SIZE = 8;

    public static void validateCourtNumber(Integer courtNumber) {
        if (courtNumber == null) {
            return;
        }
        if (courtNumber <= 0) {
            throw new CourtException(CourtErrorCode.INVALID_COURT_NUMBER);
        }
    }

    public static void validateDateInWindow(LocalDate date) {
        if (date == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate windowStart = today.minusDays(DATE_WINDOW_SIZE - 1);
        if (date.isBefore(windowStart) || date.isAfter(today)) {
            throw new StadiumException(StadiumErrorCode.INVALID_DATE);
        }
    }
}
