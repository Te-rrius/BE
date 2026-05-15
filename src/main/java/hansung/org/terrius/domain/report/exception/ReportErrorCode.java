package hansung.org.terrius.domain.report.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseResponseCode {

    MATCH_VIDEO_NOT_FOUND("REPORT_404_1", NOT_FOUND, "경기 영상을 찾을 수 없습니다."),
    REPORT_NOT_FOUND("REPORT_404_2", NOT_FOUND, "리포트를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
