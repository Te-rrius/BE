package hansung.org.terrius.domain.report.analysis.dto;

public record AnalyzeTestFixedCsvReq(
        String analysisId,
        Long matchVideoId,
        Integer poseClipsPerPlayer,
        Integer poseMinPerType,
        Integer poseLimitPerPlayer,
        Boolean skipPose,
        String posePython
) {
}
