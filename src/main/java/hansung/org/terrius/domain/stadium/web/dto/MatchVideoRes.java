package hansung.org.terrius.domain.stadium.web.dto;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.match.entity.enums.MatchType;

import java.time.LocalTime;

public record MatchVideoRes(
        Long matchVideoId,
        LocalTime startTime,
        LocalTime endTime,
        MatchType matchType
) {
    public static MatchVideoRes from(MatchVideo matchVideo) {
        return new MatchVideoRes(
                matchVideo.getId(),
                matchVideo.getStartTime(),
                matchVideo.getEndTime(),
                matchVideo.getMatchType()
        );
    }
}
