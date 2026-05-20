package hansung.org.terrius.domain.match.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.BaseResponseCode;

public class MatchException extends BaseException {
    public MatchException(BaseResponseCode baseResponseCode) {
        super(baseResponseCode);
    }
}
