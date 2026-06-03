package hansung.org.terrius.domain.report.analysis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

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
            @JsonAlias("motionAnalysisId")
            Long motionAnalysisId,
            @JsonProperty("video_url")
            @JsonAlias("videoUrl")
            String videoUrl,
            @JsonProperty("video_path")
            @JsonAlias("videoPath")
            String videoPath,
            @JsonProperty("shot_type")
            @JsonAlias("shotType")
            String shotType,
            @JsonProperty("shot_type_name")
            @JsonAlias("shotTypeName")
            String shotTypeName,
            @JsonProperty("impact_angles")
            @JsonAlias("impactAngles")
            AnalyzeImpactAnglesRes impactAngles,
            @JsonAlias("shoulderRotationAngle")
            Double shoulderRotationAngle,
            @JsonAlias("spineRotationAngle")
            Double spineRotationAngle,
            @JsonAlias("waistRotationAngle")
            Double waistRotationAngle,
            @JsonProperty("score_result")
            @JsonAlias("scoreResult")
            AnalyzeScoreResultRes scoreResult,
            @JsonProperty("shoulder_reference_value")
            @JsonAlias("shoulderReferenceValue")
            Double shoulderReferenceValue,
            @JsonProperty("shoulder_feedback")
            @JsonAlias("shoulderFeedback")
            String shoulderFeedback,
            @JsonProperty("spine_reference_value")
            @JsonAlias("spineReferenceValue")
            Double spineReferenceValue,
            @JsonProperty("spine_feedback")
            @JsonAlias("spineFeedback")
            String spineFeedback,
            @JsonProperty("waist_reference_value")
            @JsonAlias("waistReferenceValue")
            Double waistReferenceValue,
            @JsonProperty("waist_feedback")
            @JsonAlias("waistFeedback")
            String waistFeedback,
            @JsonProperty("improvement_point")
            @JsonAlias("improvementPoint")
            String improvementPoint,
            Double score
    ) {
        public String videoUrl() {
            return videoUrl != null ? videoUrl : videoPath;
        }

        public Double shoulderRotationAngle() {
            return shoulderRotationAngle != null ? shoulderRotationAngle : impactAnglesValue(impactAngles, Joint.SHOULDER);
        }

        public Double spineRotationAngle() {
            return spineRotationAngle != null ? spineRotationAngle : impactAnglesValue(impactAngles, Joint.SPINE);
        }

        public Double waistRotationAngle() {
            return waistRotationAngle != null ? waistRotationAngle : impactAnglesValue(impactAngles, Joint.WAIST);
        }

        public Double shoulderReferenceValue() {
            return shoulderReferenceValue != null ? shoulderReferenceValue : scoreItemValue(scoreResult, Joint.SHOULDER);
        }

        public String shoulderFeedback() {
            return shoulderFeedback != null ? shoulderFeedback : scoreItemFeedback(scoreResult, Joint.SHOULDER);
        }

        public Double spineReferenceValue() {
            return spineReferenceValue != null ? spineReferenceValue : scoreItemValue(scoreResult, Joint.SPINE);
        }

        public String spineFeedback() {
            return spineFeedback != null ? spineFeedback : scoreItemFeedback(scoreResult, Joint.SPINE);
        }

        public Double waistReferenceValue() {
            return waistReferenceValue != null ? waistReferenceValue : scoreItemValue(scoreResult, Joint.WAIST);
        }

        public String waistFeedback() {
            return waistFeedback != null ? waistFeedback : scoreItemFeedback(scoreResult, Joint.WAIST);
        }

        public String improvementPoint() {
            return improvementPoint != null
                    ? improvementPoint
                    : scoreResult == null ? null : scoreResult.improvementPoint();
        }

        public Double score() {
            return score != null ? score : scoreResult == null ? null : scoreResult.totalScore();
        }
    }

    public record AnalyzeImpactAnglesRes(
            @JsonProperty("shoulder_rotation")
            Double shoulderRotation,
            @JsonProperty("spine_rotation")
            Double spineRotation,
            @JsonProperty("hip_rotation")
            Double hipRotation
    ) {
    }

    public record AnalyzeScoreResultRes(
            @JsonProperty("total_score")
            @JsonAlias("totalScore")
            Double totalScore,
            AnalyzeScoreItemsRes items,
            @JsonProperty("improvement_point")
            @JsonAlias("improvementPoint")
            String improvementPoint
    ) {
    }

    public record AnalyzeScoreItemsRes(
            @JsonProperty("shoulder_rotation")
            AnalyzeScoreItemRes shoulderRotation,
            @JsonProperty("spine_rotation")
            AnalyzeScoreItemRes spineRotation,
            @JsonProperty("hip_rotation")
            AnalyzeScoreItemRes hipRotation
    ) {
    }

    public record AnalyzeScoreItemRes(
            @JsonProperty("reference_value")
            @JsonAlias("referenceValue")
            Double referenceValue,
            String feedback
    ) {
    }

    private enum Joint {
        SHOULDER,
        SPINE,
        WAIST
    }

    private static Double impactAnglesValue(AnalyzeImpactAnglesRes impactAngles, Joint joint) {
        if (impactAngles == null) {
            return null;
        }

        return switch (joint) {
            case SHOULDER -> impactAngles.shoulderRotation();
            case SPINE -> impactAngles.spineRotation();
            case WAIST -> impactAngles.hipRotation();
        };
    }

    private static AnalyzeScoreItemRes scoreItem(AnalyzeScoreResultRes scoreResult, Joint joint) {
        if (scoreResult == null || scoreResult.items() == null) {
            return null;
        }

        return switch (joint) {
            case SHOULDER -> scoreResult.items().shoulderRotation();
            case SPINE -> scoreResult.items().spineRotation();
            case WAIST -> scoreResult.items().hipRotation();
        };
    }

    private static Double scoreItemValue(AnalyzeScoreResultRes scoreResult, Joint joint) {
        AnalyzeScoreItemRes scoreItem = scoreItem(scoreResult, joint);
        return scoreItem == null ? null : scoreItem.referenceValue();
    }

    private static String scoreItemFeedback(AnalyzeScoreResultRes scoreResult, Joint joint) {
        AnalyzeScoreItemRes scoreItem = scoreItem(scoreResult, joint);
        return scoreItem == null ? null : scoreItem.feedback();
    }

    public record AnalyzeHighlightVideoRes(
            Long highlightVideoId,
            String videoType,
            String videoTypeName,
            String videoUrl
    ) {
    }
}
