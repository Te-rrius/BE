package hansung.org.terrius.global.response.code;

import static hansung.org.terrius.global.constant.StaticValue.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessResponseCode implements BaseResponseCode{
    SUCCESS_OK("GLOBAL_200", OK, "호출에 성공했습니다."),
    SUCCESS_CREATED("GLOBAL_201", CREATED, "호출에 성공했습니다.");

    private String code;
    private int httpStatus;
    private String message;
}
