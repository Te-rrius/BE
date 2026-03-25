package hansung.org.terrius.domain.user.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.domain.user.exception.UserErrorCode;
import hansung.org.terrius.domain.user.exception.UserException;
import hansung.org.terrius.domain.user.repository.UserRepository;
import hansung.org.terrius.domain.user.web.dto.LoginRes;
import hansung.org.terrius.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;

        try {
            response =
                    restTemplate.exchange(
                            "https://kauth.kakao.com/oauth/token",
                            HttpMethod.POST,
                            kakaoTokenRequest,
                            String.class);
        } catch (RestClientException e) {
            throw new UserException(UserErrorCode.KAKAO_AUTH_FAILED);
        }

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = parseJson(response.getBody());
        JsonNode accessTokenNode = jsonNode.get("access_token");
        if (accessTokenNode == null || accessTokenNode.asText().isBlank()) {
            throw new UserException(UserErrorCode.KAKAO_AUTH_FAILED);
        }
        return accessTokenNode.asText();
    }

    public HashMap<String, Object> getKakaoUserInfo(String accessToken) {

        HashMap<String, Object> userInfo = new HashMap<String, Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response =
                    restTemplate.exchange(
                            "https://kapi.kakao.com/v2/user/me",
                            HttpMethod.POST,
                            kakaoUserInfoRequest,
                            String.class);
        } catch (RestClientException e) {
            throw new UserException(UserErrorCode.KAKAO_USER_INFO_FETCH_FAILED);
        }

        // responseBody에 있는 정보 추출
        JsonNode jsonNode = parseJson(response.getBody());

        JsonNode idNode = jsonNode.get("id");
        JsonNode emailNode = jsonNode.path("kakao_account").get("email");
        JsonNode nicknameNode = jsonNode.path("properties").get("nickname");
        if (idNode == null || emailNode == null || nicknameNode == null) {
            throw new UserException(UserErrorCode.KAKAO_ACCOUNT_INFO_MISSING);
        }

        Long id = idNode.asLong();
        String email = emailNode.asText();
        String nickname = nicknameNode.asText();

        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("nickname", nickname);

        return userInfo;
    }

    public LoginRes kakaoUserLogin(HashMap<String, Object> userInfo) {

        User kakaoUser = userRepository.findByEmail(userInfo.get("email").toString()).orElse(null);
        User newUser;

        if (kakaoUser == null) {
            newUser = userRepository.save(KakaoAuthConverter.toUser(userInfo));
        } else {
            newUser = kakaoUser;
        }

        String accessToken = jwtTokenProvider.generateAccessToken(newUser.getId());

        return KakaoAuthConverter.toLoginResponse(accessToken, newUser);
    }

    private JsonNode parseJson(String responseBody) {
        try {
            return new ObjectMapper().readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new UserException(UserErrorCode.KAKAO_RESPONSE_PARSE_FAILED);
        }
    }
}
