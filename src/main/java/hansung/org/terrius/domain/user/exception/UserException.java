package hansung.org.terrius.domain.user.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.BaseResponseCode;

public class UserException extends BaseException {
    public UserException(BaseResponseCode baseResponseCode) {
        super(baseResponseCode);
    }
}
