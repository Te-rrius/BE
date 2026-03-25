package hansung.org.terrius.domain.user.web.dto;

public record LoginRes(
        Long id,
        String name,
        AuthTokens token
) {
}

