package hansung.org.terrius.domain.report.entity;

import hansung.org.terrius.domain.report.entity.enums.HighlightVideoType;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "highlight_videos")
public class HighlightVideo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_type", nullable = false)
    private HighlightVideoType videoType;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    public static HighlightVideo create(Report report, HighlightVideoType videoType, String videoUrl) {
        HighlightVideo highlightVideo = HighlightVideo.builder()
                .report(report)
                .videoType(videoType)
                .videoUrl(videoUrl)
                .build();

        report.addHighlightVideo(highlightVideo);

        return highlightVideo;
    }
}
