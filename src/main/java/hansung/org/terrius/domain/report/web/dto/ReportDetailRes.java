package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
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
        ReportTarget target,
        String targetName,
        ShotType shotType,
        String shotTypeName,
        Double maxSpeed,
        Double shoulderRotationAngle,
        Double spineRotationAngle,
        Double waistRotationAngle,
        Double averageRallyCount,
        Integer maxRallyCount,
        Integer minRallyCount,
        Integer totalShotCount,
        String improvementPoint,
        Double firstServeSuccessRate,
        Double secondServeSuccessRate,
        Double firstServeRate,
        List<ReportMaterialRes> materials
) {
    public static ReportDetailRes from(Report report) {
        return ReportDetailRes.builder()
                .reportId(report.getId())
                .matchVideoId(report.getMatchVideo().getId())
                .matchDate(report.getMatchVideo().getMatchDate())
                .startTime(report.getMatchVideo().getStartTime())
                .endTime(report.getMatchVideo().getEndTime())
                .stadiumName(extractStadiumName(report.getMatchVideo()))
                .target(report.getTarget())
                .targetName(report.getTarget().getDescription())
                .shotType(report.getShotType())
                .shotTypeName(report.getShotType().getDescription())
                .maxSpeed(report.getMaxSpeed())
                .shoulderRotationAngle(report.getShoulderRotationAngle())
                .spineRotationAngle(report.getSpineRotationAngle())
                .waistRotationAngle(report.getWaistRotationAngle())
                .averageRallyCount(report.getAverageRallyCount())
                .maxRallyCount(report.getMaxRallyCount())
                .minRallyCount(report.getMinRallyCount())
                .totalShotCount(report.getTotalShotCount())
                .improvementPoint(report.getImprovementPoint())
                .firstServeSuccessRate(report.getFirstServeSuccessRate())
                .secondServeSuccessRate(report.getSecondServeSuccessRate())
                .firstServeRate(report.getFirstServeRate())
                .materials(report.getReportMaterials().stream()
                        .map(ReportMaterialRes::from)
                        .toList())
                .build();
    }

    private static String extractStadiumName(MatchVideo matchVideo) {
        try {
            Object stadium = matchVideo.getClass().getMethod("getStadium").invoke(matchVideo);
            if (stadium == null) {
                return null;
            }
            Object name = stadium.getClass().getMethod("getName").invoke(stadium);
            return name == null ? null : name.toString();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
