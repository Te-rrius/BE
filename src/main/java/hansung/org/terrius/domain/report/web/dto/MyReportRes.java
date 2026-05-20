package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.ReportOwnership;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MyReportRes(
        Long reportId,
        Long matchVideoId,
        LocalDate matchDate
) {
    public static MyReportRes from(ReportOwnership reportOwnership) {
        Report report = reportOwnership.getReport();
        MatchVideo matchVideo = report.getMatchVideo();

        return MyReportRes.builder()
                .reportId(report.getId())
                .matchVideoId(matchVideo.getId())
                .matchDate(matchVideo.getMatchDate())
                .build();
    }
}
