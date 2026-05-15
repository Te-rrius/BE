package hansung.org.terrius.domain.user.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.BAD_REQUEST;
import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;
import static hansung.org.terrius.global.constant.StaticValue.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseResponseCode {

    INVALID_CREDENTIALS("USER_401_1", UNAUTHORIZED, "사용자 인증 정보가 유효하지 않습니다."),
    KAKAO_AUTH_FAILED("USER_401_2", UNAUTHORIZED, "카카오 인증에 실패했습니다."),
    KAKAO_USER_INFO_FETCH_FAILED("USER_401_3", UNAUTHORIZED, "카카오 사용자 정보를 가져오지 못했습니다."),
    KAKAO_RESPONSE_PARSE_FAILED("USER_400_1", BAD_REQUEST, "카카오 응답을 해석하지 못했습니다."),
    KAKAO_ACCOUNT_INFO_MISSING("USER_400_2", BAD_REQUEST, "카카오 계정 정보가 올바르지 않습니다."),
    USER_NOT_FOUND("USER_404_1", NOT_FOUND, "사용자를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
