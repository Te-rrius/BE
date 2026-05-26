package hansung.org.terrius.domain.report.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HighlightVideoType {
    WINNING_SHOT("위닝샷"),
    WORST_SHOT("워스트샷");

    private final String description;
}
