package hansung.org.terrius.domain.stadium.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.BaseResponseCode;

public class StadiumException extends BaseException {
    public StadiumException(BaseResponseCode baseResponseCode) { super(baseResponseCode); }
}
