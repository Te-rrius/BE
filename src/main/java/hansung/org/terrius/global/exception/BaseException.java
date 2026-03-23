package hansung.org.terrius.global.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private final BaseResponseCode baseResponseCode;
}
