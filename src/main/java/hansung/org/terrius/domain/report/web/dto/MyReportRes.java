package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.report.entity.ReportOwnership;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MyReportRes(
        Long reportId,
        LocalDate matchDate
) {
    public static MyReportRes from(ReportOwnership reportOwnership) {
        return MyReportRes.builder()
                .reportId(reportOwnership.getReport().getId())
                .matchDate(reportOwnership.getReport().getMatchVideo().getMatchDate())
                .build();
    }
}
