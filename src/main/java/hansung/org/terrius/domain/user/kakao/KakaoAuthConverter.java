package hansung.org.terrius.domain.user.kakao;

import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.domain.user.web.dto.AuthTokens;
import hansung.org.terrius.domain.user.web.dto.LoginRes;

import java.util.HashMap;

public final class KakaoAuthConverter {

    private KakaoAuthConverter() {
    }

    public static User toUser(HashMap<String, Object> userInfo) {
        return User.builder()
                .email(userInfo.get("email").toString())
                .nicName(userInfo.get("nickname").toString())
                .build();
    }

    public static LoginRes toLoginResponse(String accessToken, User user) {
        return new LoginRes(
                user.getId(),
                user.getNicName(),
                new AuthTokens(accessToken)
        );
    }
}
