package hansung.org.terrius.domain.report.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShotType {
    SERVE("서브"),
    FOREHAND("포핸드"),
    BACKHAND("백핸드"),
    VOLLEY("발리"),
    SMASH("스매시");

    private final String description;
}
