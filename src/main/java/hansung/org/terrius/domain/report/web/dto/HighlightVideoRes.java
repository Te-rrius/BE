package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.report.entity.HighlightVideo;
import hansung.org.terrius.domain.report.entity.enums.HighlightVideoType;
import lombok.Builder;

@Builder
public record HighlightVideoRes(
        Long highlightVideoId,
        HighlightVideoType videoType,
        String videoTypeName,
        String videoUrl
) {
    public static HighlightVideoRes from(HighlightVideo highlightVideo) {
        return HighlightVideoRes.builder()
                .highlightVideoId(highlightVideo.getId())
                .videoType(highlightVideo.getVideoType())
                .videoTypeName(highlightVideo.getVideoType().getDescription())
                .videoUrl(highlightVideo.getVideoUrl())
                .build();
    }
}
