package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record ReportDetailRes(
        Long reportId,
        Long matchVideoId,
        LocalDate matchDate,
        LocalTime startTime,
        LocalTime endTime,
        String stadiumName,
        Integer courtNumber,
        ReportTarget target,
        String targetName,
        Double maxSpeed,
        Double averageRallyCount,
        Integer maxRallyCount,
        Integer minRallyCount,
        Integer totalShotCount,
        Double firstServeSuccessRate,
        Double secondServeSuccessRate,
        Double firstServeRate,
        List<MotionAnalysisRes> motionAnalyses,
        List<HighlightVideoRes> highlightVideos
) {
    public static ReportDetailRes from(Report report) {
        return ReportDetailRes.builder()
                .reportId(report.getId())
                .matchVideoId(report.getMatchVideo().getId())
                .matchDate(report.getMatchVideo().getMatchDate())
                .startTime(report.getMatchVideo().getStartTime())
                .endTime(report.getMatchVideo().getEndTime())
                .stadiumName(report.getMatchVideo().getCourt().getStadium().getName())
                .courtNumber(report.getMatchVideo().getCourt().getCourtNumber())
                .target(report.getTarget())
                .targetName(report.getTarget().getDescription())
                .maxSpeed(report.getMaxSpeed())
                .averageRallyCount(report.getAverageRallyCount())
                .maxRallyCount(report.getMaxRallyCount())
                .minRallyCount(report.getMinRallyCount())
                .totalShotCount(report.getTotalShotCount())
                .firstServeSuccessRate(report.getFirstServeSuccessRate())
                .secondServeSuccessRate(report.getSecondServeSuccessRate())
                .firstServeRate(report.getFirstServeRate())
                .motionAnalyses(report.getMotionAnalyses().stream()
                        .map(MotionAnalysisRes::from)
                        .toList())
                .highlightVideos(report.getHighlightVideos().stream()
                        .map(HighlightVideoRes::from)
                        .toList())
                .build();
    }
}
