package hansung.org.terrius.domain.report.analysis.dto;

public record AnalyzeMatchReq(
        String videoUrl,
        Long matchVideoId,
        String analysisId,
        String configUrl,
        String configPath,
        String ballCsvUrl,
        String playerCsvUrl
) {
}
