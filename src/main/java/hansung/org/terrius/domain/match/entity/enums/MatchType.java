package hansung.org.terrius.domain.match.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchType {
    SINGLES("단식"),
    DOUBLES("복식");

    private final String description;
}
