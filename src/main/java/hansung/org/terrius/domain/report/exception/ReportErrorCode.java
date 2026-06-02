package hansung.org.terrius.domain.report.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.FORBIDDEN;
import static hansung.org.terrius.global.constant.StaticValue.INTERNAL_SERVER_ERROR;
import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseResponseCode {

    REPORT_ACCESS_DENIED("REPORT_403_1", FORBIDDEN, "해당 리포트에 접근할 권한이 없습니다."),
    REPORT_NOT_FOUND("REPORT_404_1", NOT_FOUND, "리포트를 찾을 수 없습니다."),
    REPORT_ANALYSIS_RESPONSE_INVALID("REPORT_500_1", INTERNAL_SERVER_ERROR, "분석 서버 응답 형식이 올바르지 않습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
