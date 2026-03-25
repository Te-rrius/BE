package hansung.org.terrius.domain.user.kakao;

import hansung.org.terrius.domain.user.web.dto.LoginRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

@Component
public class KakaoAuthProvider {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
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
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.KAKAO_AUTH_ERROR);
        }

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new GlobalException(GlobalErrorCode.KAKAO_AUTH_ERROR);
        }
        return jsonNode.get("access_token").asText(); // 토큰 추출
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
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.POST,
                        kakaoUserInfoRequest,
                        String.class);

        // responseBody에 있는 정보 추출
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new GlobalException(GlobalErrorCode.KAKAO_AUTH_ERROR);
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

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
        String refreshToken = jwtTokenProvider.generateRefreshToken(newUser.getId());

        refreshTokenService.saveRefreshToken(refreshToken);

        return KakaoAuthConverter.toLoginResponse(accessToken, refreshToken, newUser);
    }
}
