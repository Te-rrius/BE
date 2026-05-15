package hansung.org.terrius.domain.user.entity;

import hansung.org.terrius.domain.user.entity.enums.UserGrade;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_grade", nullable = false)
    private UserGrade userGrade = UserGrade.NORMAL;

    public void updateKakaoProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
