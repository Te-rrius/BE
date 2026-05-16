package hansung.org.terrius.domain.report.exception;

import hansung.org.terrius.global.exception.BaseException;
import hansung.org.terrius.global.response.code.BaseResponseCode;

public class ReportException extends BaseException {

    public ReportException(BaseResponseCode baseResponseCode) {
        super(baseResponseCode);
    }
}
