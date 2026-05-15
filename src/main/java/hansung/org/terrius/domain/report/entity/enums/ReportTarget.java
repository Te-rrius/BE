package hansung.org.terrius.domain.report.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportTarget {
    PLAYER_ONE("1번 선수"),
    PLAYER_TWO("2번 선수");

    private final String description;
}
