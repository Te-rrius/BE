package hansung.org.terrius.domain.user.kakao;

import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.domain.user.exception.UserErrorCode;
import hansung.org.terrius.domain.user.exception.UserException;
import hansung.org.terrius.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // Spring Security 기본 구현체가 카카오 UserInfo API를 호출하고 OAuth2User로 변환한다.
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 인가 코드로 받은 카카오 access token을 사용해 카카오 사용자 정보를 조회한다.
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 카카오 응답은 kakao_account, profile, properties처럼 중첩된 Map 구조로 내려온다.
        Map<String, Object> kakaoAccount = getMap(attributes, "kakao_account");
        Map<String, Object> profile = getMap(kakaoAccount, "profile");
        Map<String, Object> properties = getMap(attributes, "properties");

        String email = getString(kakaoAccount, "email");

        // 닉네임/프로필 이미지는 카카오 응답 위치가 다를 수 있어 properties를 우선 보고 profile을 fallback으로 사용한다.
        String nickname = firstNonBlank(
                getString(properties, "nickname"),
                getString(profile, "nickname")
        );
        String profileImageUrl = firstNonBlank(
                getString(properties, "profile_image"),
                getString(profile, "profile_image_url")
        );

        if (email == null || nickname == null) {
            throw new UserException(UserErrorCode.KAKAO_ACCOUNT_INFO_MISSING);
        }

        // 이메일 기준으로 기존 회원이면 프로필을 갱신하고, 신규 회원이면 저장한다.
        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    existingUser.updateKakaoProfile(nickname, profileImageUrl);
                    return existingUser;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .profileImageUrl(profileImageUrl)
                        .build()));

        // 성공 핸들러에서 우리 서비스 JWT를 만들 수 있도록 DB userId를 OAuth2User attributes에 추가한다.
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("userId", user.getId());

        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                customAttributes,
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(Map<String, Object> attributes, String key) {
        // 중첩 Map이 없을 때 빈 Map을 반환해 null 체크 분기를 줄인다.
        Object value = attributes.get(key);
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Map.of();
    }

    private String getString(Map<String, Object> attributes, String key) {
        // 값이 없거나 빈 문자열이면 필수값 검증/fallback에서 다루기 쉽도록 null로 통일한다.
        Object value = attributes.get(key);
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        return text;
    }

    private String firstNonBlank(String first, String second) {
        return first != null ? first : second;
    }
}
