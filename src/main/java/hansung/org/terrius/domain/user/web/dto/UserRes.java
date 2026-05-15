package hansung.org.terrius.domain.user.web.dto;

import hansung.org.terrius.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserRes(
        String nicName,
        String email,
        String profileImageUrl,
        String userGrade
) {
    public static UserRes from(User user) {
        return UserRes.builder()
                .nicName(user.getNicName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .userGrade(user.getUserGrade().getDescription())
                .build();
    }
}
