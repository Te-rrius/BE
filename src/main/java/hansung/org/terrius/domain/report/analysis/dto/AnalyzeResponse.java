package hansung.org.terrius.domain.report.analysis.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AnalyzeResponse(
        String analysisId,
        Long matchVideoId,
        String status,
        String summaryUrl,
        List<AnalyzePlayerRes> players
) {

    public record AnalyzePlayerRes(
            Long reportId,
            Long matchVideoId,
            LocalDate matchDate,
            LocalTime startTime,
            LocalTime endTime,
            String stadiumName,
            Integer courtNumber,
            String target,
            String targetName,
            Double maxSpeed,
            Double averageRallyCount,
            Integer maxRallyCount,
            Integer minRallyCount,
            Integer totalShotCount,
            Double firstServeSuccessRate,
            Double secondServeSuccessRate,
            Double firstServeRate,
            List<AnalyzeMotionAnalysisRes> motionAnalyses,
            List<AnalyzeHighlightVideoRes> highlightVideos
    ) {
    }

    public record AnalyzeMotionAnalysisRes(
            Long motionAnalysisId,
            String videoUrl,
            String shotType,
            String shotTypeName,
            Double shoulderRotationAngle,
            Double spineRotationAngle,
            Double waistRotationAngle,
            String improvementPoint,
            Double score
    ) {
    }

    public record AnalyzeHighlightVideoRes(
            Long highlightVideoId,
            String videoType,
            String videoTypeName,
            String videoUrl
    ) {
    }
}
