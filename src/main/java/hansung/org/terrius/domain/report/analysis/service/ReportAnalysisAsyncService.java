package hansung.org.terrius.domain.report.analysis.service;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.match.exception.MatchErrorCode;
import hansung.org.terrius.domain.match.exception.MatchException;
import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.report.analysis.client.FastApiAnalysisClient;
import hansung.org.terrius.domain.report.analysis.dto.AnalyzeMatchReq;
import hansung.org.terrius.domain.report.analysis.dto.AnalyzeResponse;
import hansung.org.terrius.domain.report.entity.HighlightVideo;
import hansung.org.terrius.domain.report.entity.MotionAnalysis;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.enums.HighlightVideoType;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
import hansung.org.terrius.domain.report.exception.ReportErrorCode;
import hansung.org.terrius.domain.report.exception.ReportException;
import hansung.org.terrius.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportAnalysisAsyncService {

    private final FastApiAnalysisClient fastApiAnalysisClient;
    private final MatchVideoRepository matchVideoRepository;
    private final ReportRepository reportRepository;
    private final TransactionTemplate transactionTemplate;

    @Async("reportAnalysisTaskExecutor")
    public void analyzeAndSave(Long matchVideoId) {
        try {
            MatchVideoSnapshot snapshot = transactionTemplate.execute(status -> loadSnapshot(matchVideoId));
            AnalyzeResponse analyzeResponse = fastApiAnalysisClient.analyzeMatch(createAnalyzeMatchReq(snapshot));

            transactionTemplate.executeWithoutResult(status -> saveReports(matchVideoId, analyzeResponse));
        } catch (Exception e) {
            log.warn("Failed to analyze report. matchVideoId={}", matchVideoId, e);
            transactionTemplate.executeWithoutResult(status -> cancelReportRequest(matchVideoId));
        }
    }

    private MatchVideoSnapshot loadSnapshot(Long matchVideoId) {
        MatchVideo matchVideo = matchVideoRepository.findById(matchVideoId)
                .orElseThrow(() -> new MatchException(MatchErrorCode.MATCH_VIDEO_NOT_FOUND));

        return new MatchVideoSnapshot(matchVideo.getId(), matchVideo.getVideoUrl());
    }

    private AnalyzeMatchReq createAnalyzeMatchReq(MatchVideoSnapshot snapshot) {
        return new AnalyzeMatchReq(
                snapshot.videoUrl(),
                snapshot.matchVideoId(),
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                null
        );
    }

    private void saveReports(Long matchVideoId, AnalyzeResponse analyzeResponse) {
        if (reportRepository.existsByMatchVideoId(matchVideoId)) {
            return;
        }

        MatchVideo matchVideo = matchVideoRepository.findById(matchVideoId)
                .orElseThrow(() -> new MatchException(MatchErrorCode.MATCH_VIDEO_NOT_FOUND));

        List<Report> reports = createReports(matchVideo, analyzeResponse);
        reportRepository.saveAll(reports);
    }

    private void cancelReportRequest(Long matchVideoId) {
        if (reportRepository.existsByMatchVideoId(matchVideoId)) {
            return;
        }

        matchVideoRepository.findById(matchVideoId)
                .ifPresent(MatchVideo::cancelReportRequest);
    }

    private List<Report> createReports(MatchVideo matchVideo, AnalyzeResponse analyzeResponse) {
        if (analyzeResponse == null || analyzeResponse.players() == null || analyzeResponse.players().isEmpty()) {
            throw new ReportException(ReportErrorCode.REPORT_ANALYSIS_RESPONSE_INVALID);
        }

        if (analyzeResponse.matchVideoId() != null && !analyzeResponse.matchVideoId().equals(matchVideo.getId())) {
            throw new ReportException(ReportErrorCode.REPORT_ANALYSIS_RESPONSE_INVALID);
        }

        return analyzeResponse.players().stream()
                .map(player -> createReport(matchVideo, player))
                .toList();
    }

    private Report createReport(MatchVideo matchVideo, AnalyzeResponse.AnalyzePlayerRes player) {
        Report report = Report.builder()
                .maxSpeed(player.maxSpeed())
                .averageRallyCount(player.averageRallyCount())
                .maxRallyCount(player.maxRallyCount())
                .minRallyCount(player.minRallyCount())
                .totalShotCount(player.totalShotCount())
                .firstServeSuccessRate(player.firstServeSuccessRate())
                .secondServeSuccessRate(player.secondServeSuccessRate())
                .firstServeRate(player.firstServeRate())
                .target(parseEnum(ReportTarget.class, player.target()))
                .build();

        report.assignMatchVideo(matchVideo);

        if (player.motionAnalyses() != null) {
            player.motionAnalyses().forEach(motionAnalysis -> MotionAnalysis.create(
                    report,
                    motionAnalysis.videoUrl(),
                    parseEnum(ShotType.class, motionAnalysis.shotType()),
                    motionAnalysis.shoulderRotationAngle(),
                    motionAnalysis.spineRotationAngle(),
                    motionAnalysis.waistRotationAngle(),
                    motionAnalysis.shoulderReferenceValue(),
                    motionAnalysis.shoulderFeedback(),
                    motionAnalysis.spineReferenceValue(),
                    motionAnalysis.spineFeedback(),
                    motionAnalysis.waistReferenceValue(),
                    motionAnalysis.waistFeedback(),
                    motionAnalysis.improvementPoint(),
                    motionAnalysis.score()
            ));
        }

        if (player.highlightVideos() != null) {
            player.highlightVideos().forEach(highlightVideo -> HighlightVideo.create(
                    report,
                    parseEnum(HighlightVideoType.class, highlightVideo.videoType()),
                    highlightVideo.videoUrl()
            ));
        }

        return report;
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (RuntimeException e) {
            throw new ReportException(ReportErrorCode.REPORT_ANALYSIS_RESPONSE_INVALID);
        }
    }

    private record MatchVideoSnapshot(Long matchVideoId, String videoUrl) {
    }
}
