package hansung.org.terrius.global.jwt.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.ErrorResponseCode;

public class TokenNotMatchException extends BaseException {
    public TokenNotMatchException() { super(ErrorResponseCode.TOKEN_NOT_MATCH); }
}
