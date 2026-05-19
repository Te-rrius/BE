package hansung.org.terrius.domain.stadium.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.BAD_REQUEST;
import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum StadiumErrorCode implements BaseResponseCode {

    INVALID_DATE("STADIUM_400_1", BAD_REQUEST, "유효하지 않은 날짜입니다."),
    STADIUM_NOT_FOUND("STADIUM_404_1", NOT_FOUND, "구장을 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
