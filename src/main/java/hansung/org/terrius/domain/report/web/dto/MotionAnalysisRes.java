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
        Double shoulderReferenceValue,
        String shoulderFeedback,
        Double spineReferenceValue,
        String spineFeedback,
        Double waistReferenceValue,
        String waistFeedback,
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
                .shoulderReferenceValue(motionAnalysis.getShoulderReferenceValue())
                .shoulderFeedback(motionAnalysis.getShoulderFeedback())
                .spineReferenceValue(motionAnalysis.getSpineReferenceValue())
                .spineFeedback(motionAnalysis.getSpineFeedback())
                .waistReferenceValue(motionAnalysis.getWaistReferenceValue())
                .waistFeedback(motionAnalysis.getWaistFeedback())
                .improvementPoint(motionAnalysis.getImprovementPoint())
                .score(motionAnalysis.getScore())
                .build();
    }
}
