package hansung.org.terrius.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.SignatureException;
import hansung.org.terrius.global.jwt.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final JwtParser parser;

    // JWT를 서명 검증과 함께 파싱하여 Claims를 반환
    public Claims parseClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    // 토큰의 subject(userId) 추출
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    // 만료 시각을 기준으로 토큰이 만료됐는지 판단
    public boolean isExpired(String token) {
        Date exp = parseClaims(token).getExpiration();
        return exp != null && exp.before(new Date());
    }

    // 토큰 유효성(서명, 만료, 형식)을 검사 (예외를 던짐)
    public void validateTokenThrows(String token) {
        try {
            parseClaims(token);

        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidException();
        }
    }

    // 토큰 유효성(서명, 만료, 형식)을 검사 (boolean 반환)
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) { // 만료된 토큰
            return false;
        } catch (JwtException | IllegalArgumentException e) { // 서명/만료 외 기타 예외
            return false;
        }
    }

    // Authorization 헤더에서 Bearer 토큰 문자열을 추출
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}
