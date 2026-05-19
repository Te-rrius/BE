package hansung.org.terrius.domain.stadium.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.BAD_REQUEST;
import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum CourtErrorCode implements BaseResponseCode {

    INVALID_COURT_NUMBER("STADIUM_400_1", BAD_REQUEST, "유효하지 않은 코트 번호입니다."),
    COURT_NOT_FOUND("COURT_404_1", NOT_FOUND, "해당 구장에 존재하지 않는 코트입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
