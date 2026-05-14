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
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = Long.valueOf(oauth2User.getAttribute("userId").toString());
        String accessToken = jwtTokenProvider.generateAccessToken(userId);

        String redirectUri = UriComponentsBuilder.fromUriString(frontendCallbackUri)
                .queryParam("accessToken", accessToken)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUri);
    }
}
