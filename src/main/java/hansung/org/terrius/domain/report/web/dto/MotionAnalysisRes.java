package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.report.entity.MotionAnalysis;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
import lombok.Builder;

@Builder
public record MotionAnalysisRes(
        Long motionAnalysisId,
        String videoUrl,
        ShotType shotType,
        String shotTypeName,
        Double shoulderRotationAngle,
        Double spineRotationAngle,
        Double waistRotationAngle,
        String improvementPoint,
        Double score
) {
    public static MotionAnalysisRes from(MotionAnalysis motionAnalysis) {
        return MotionAnalysisRes.builder()
                .motionAnalysisId(motionAnalysis.getId())
                .videoUrl(motionAnalysis.getVideoUrl())
                .shotType(motionAnalysis.getShotType())
                .shotTypeName(motionAnalysis.getShotType().getDescription())
                .shoulderRotationAngle(motionAnalysis.getShoulderRotationAngle())
                .spineRotationAngle(motionAnalysis.getSpineRotationAngle())
                .waistRotationAngle(motionAnalysis.getWaistRotationAngle())
                .improvementPoint(motionAnalysis.getImprovementPoint())
                .score(motionAnalysis.getScore())
                .build();
    }
}
