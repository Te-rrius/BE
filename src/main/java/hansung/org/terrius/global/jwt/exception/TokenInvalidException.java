package hansung.org.terrius.global.jwt.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.ErrorResponseCode;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() { super(ErrorResponseCode.INVALID_TOKEN); }
}
