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
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        Map<String, Object> kakaoAccount = getMap(attributes, "kakao_account");
        Map<String, Object> profile = getMap(kakaoAccount, "profile");
        Map<String, Object> properties = getMap(attributes, "properties");

        String email = getString(kakaoAccount, "email");
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

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    existingUser.updateKakaoProfile(nickname, profileImageUrl);
                    return existingUser;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nicName(nickname)
                        .profileImageUrl(profileImageUrl)
                        .build()));

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
        Object value = attributes.get(key);
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Map.of();
    }

    private String getString(Map<String, Object> attributes, String key) {
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
