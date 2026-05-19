package hansung.org.terrius.domain.stadium.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum StadiumErrorCode implements BaseResponseCode {

    STADIUM_NOT_FOUND("STADIUM_404_1", NOT_FOUND, "구장을 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
