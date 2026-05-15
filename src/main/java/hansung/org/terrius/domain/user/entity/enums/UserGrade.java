package hansung.org.terrius.domain.user.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGrade {
    NORMAL("일반회원");

    private final String description;
}
