package hansung.org.terrius.domain.stadium.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.BaseResponseCode;

public class CourtException extends BaseException {
    public CourtException(BaseResponseCode baseResponseCode) {
        super(baseResponseCode);
    }
}
