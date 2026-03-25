package hansung.org.terrius.domain.user.service;

import hansung.org.terrius.domain.user.kakao.KakaoAuthProvider;
import hansung.org.terrius.domain.user.web.dto.LoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final KakaoAuthProvider kakaoAuthProvider;

    @Override
    public LoginRes login(String code) {
        // 1. 인가 코드로 토큰 발급
        String accessToken = kakaoAuthProvider.getAccessToken(code);

        // 2. 토큰으로 카카오 유저 정보 가져오기
        HashMap<String, Object> userInfo = kakaoAuthProvider.getKakaoUserInfo(accessToken);

        // 3. 카카오 유저 정보로 로그인
        LoginRes kakaoUserRes = kakaoAuthProvider.kakaoUserLogin(userInfo);

        return kakaoUserRes;
    }
}
