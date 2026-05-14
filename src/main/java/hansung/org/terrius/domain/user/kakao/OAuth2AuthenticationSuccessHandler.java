package hansung.org.terrius.domain.user.kakao;

import hansung.org.terrius.global.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${frontend.oauth-callback-uri}")
    private String frontendCallbackUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // KakaoOAuth2UserService에서 attributes에 넣어둔 DB userId를 꺼낸다.
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = Long.valueOf(oauth2User.getAttribute("userId").toString());

        // 카카오 access token이 아니라 우리 서비스에서 사용할 JWT access token을 발급한다.
        String accessToken = jwtTokenProvider.generateAccessToken(userId);

        // 로그인 성공 후 프론트 콜백 페이지가 토큰을 읽을 수 있도록 쿼리 파라미터로 전달한다.
        String redirectUri = UriComponentsBuilder.fromUriString(frontendCallbackUri)
                .queryParam("accessToken", accessToken)
                .build()
                .encode()
                .toUriString();

        // JSON 응답이 아니라 브라우저를 프론트 콜백 URL로 이동시킨다.
        response.sendRedirect(redirectUri);
    }
}
