package hansung.org.terrius.domain.match.exception;

import hansung.org.terrius.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static hansung.org.terrius.global.constant.StaticValue.CONFLICT;
import static hansung.org.terrius.global.constant.StaticValue.FORBIDDEN;
import static hansung.org.terrius.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements BaseResponseCode {

    MATCH_VIDEO_STADIUM_MISMATCH("MATCH_403_1", FORBIDDEN, "해당 구장에 속한 경기 영상이 아닙니다."),
    MATCH_VIDEO_NOT_FOUND("MATCH_404_1", NOT_FOUND, "경기 영상을 찾을 수 없습니다."),
    MATCH_VIDEO_ALREADY_REQUESTED("MATCH_409_1", CONFLICT, "이미 리포트가 신청된 경기 영상입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
