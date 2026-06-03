package hansung.org.terrius.global.init;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.match.entity.enums.MatchType;
import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.report.entity.HighlightVideo;
import hansung.org.terrius.domain.report.entity.MotionAnalysis;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.enums.HighlightVideoType;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
import hansung.org.terrius.domain.report.repository.HighlightVideoRepository;
import hansung.org.terrius.domain.report.repository.ReportRepository;
import hansung.org.terrius.domain.stadium.entity.Court;
import hansung.org.terrius.domain.stadium.entity.Stadium;
import hansung.org.terrius.domain.stadium.repository.CourtRepository;
import hansung.org.terrius.domain.stadium.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final StadiumRepository stadiumRepository;
    private final CourtRepository courtRepository;
    private final MatchVideoRepository matchVideoRepository;
    private final ReportRepository reportRepository;
    private final HighlightVideoRepository highlightVideoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initStadiums();
        initCourts();
        initReports();
    }

    private void initStadiums() {
        if (stadiumRepository.count() > 0) {
            return;
        }

        stadiumRepository.saveAll(List.of(
                Stadium.builder()
                        .name("테리우스 잠실 테니스파크")
                        .imageUrl("https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/tennis_1.jpg")
                        .province("서울특별시")
                        .city("송파구")
                        .address("서울특별시 송파구 올림픽로 25")
                        .build(),
                Stadium.builder()
                        .name("테리우스 수원 실내테니스장")
                        .imageUrl("https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/tennis_3.png")
                        .province("경기도")
                        .city("수원시 팔달구")
                        .address("경기도 수원시 팔달구 효원로 241 테리우스 수원 실내테니스장")
                        .build(),
                Stadium.builder()
                        .name("테리우스 강남 테니스클럽")
                        .imageUrl("https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/tennis_2.jpg")
                        .province("서울특별시")
                        .city("강남구")
                        .address("서울특별시 강남구 테헤란로 123 테리우스 강남 테니스클럽")
                        .build()
        ));
    }

    private void initCourts() {
        if (courtRepository.count() > 0) {
            return;
        }

        List<Stadium> stadiums = stadiumRepository.findAll();
        // 구장별 코트 수: 1번 구장 = 4개, 2번 구장 = 3개, 3번 구장 = 5개
        int[] courtCounts = {4, 3, 5};
        List<Court> courts = new ArrayList<>();
        for (int s = 0; s < stadiums.size(); s++) {
            Stadium stadium = stadiums.get(s);
            int count = (s < courtCounts.length) ? courtCounts[s] : 4;
            for (int i = 1; i <= count; i++) {
                Court court = Court.builder()
                        .courtNumber(i)
                        .build();
                court.assignStadium(stadium);
                courts.add(court);
            }
        }
        courtRepository.saveAll(courts);
    }

    private void initReports() {
        String scoringStrokeUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/06_scoring_stroke.mp4";
        String playerAPipelineUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/player-A-pipeline.mp4";
        String playerBPipelineUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/player-B-pipeline.mp4";
        String playerBBackhandUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/player_B_back.mp4";
        String playerABestUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_A_best_01.mp4";
        String playerABestUrl2 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_A_best_02.mp4";
        String playerABestUrl3 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_A_best_03.mp4";
        String playerAWorstUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_a_worst_01.mp4";
        String playerBBestUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_b_best_01.mp4";
        String playerBWorstUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/cut_video_player_b_worst_01.mp4";
        String report2PlayerAWinUrl1 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerA-win-1.mp4";
        String report2PlayerAWinUrl2 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerA-win-2.mp4";
        String report2PlayerAWinUrl3 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerA-worst-1.mp4";
        String report2PlayerAWorstUrl1 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerA-worst-2.mp4";
        String report2PlayerAWorstUrl2 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerA-worst-3.mp4";
        String report2PlayerBWinUrl1 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerB-win-1.mp4";
        String report2PlayerBWinUrl2 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerB-win-2.mp4";
        String report2PlayerBWinUrl3 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerB-win-3.mp4";
        String report2PlayerBWorstUrl1 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerB-worst-1.mp4";
        String report2PlayerBWorstUrl2 = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/report2-playerB-worst-2.mp4";
        MatchVideo matchVideo = getOrCreateMatchVideo(scoringStrokeUrl);
        MatchVideo pipelineMatchVideo = getOrCreatePipelineMatchVideo(scoringStrokeUrl);

        if (reportRepository.count() > 0) {
            return;
        }

        Report playerOneReport = Report.builder()
                .maxSpeed(128.7)
                .averageRallyCount(5.6)
                .maxRallyCount(13)
                .minRallyCount(2)
                .totalShotCount(86)
                .firstServeSuccessRate(68.5)
                .secondServeSuccessRate(82.1)
                .firstServeRate(71.3)
                .target(ReportTarget.PLAYER_ONE)
                .build();
        playerOneReport.assignMatchVideo(matchVideo);
        MotionAnalysis.create(
                playerOneReport,
                scoringStrokeUrl,
                ShotType.FOREHAND,
                74.2,
                41.5,
                58.8,
                "임팩트 직전 상체가 먼저 열리는 경향이 있어 허리 회전 타이밍을 늦추는 연습이 필요합니다.",
                86.5
        );
        MotionAnalysis.create(
                playerOneReport,
                scoringStrokeUrl,
                ShotType.FOREHAND,
                71.8,
                39.6,
                55.2,
                "팔로스루 구간에서 라켓 궤도가 짧아지는 경향이 있어 임팩트 이후 스윙을 끝까지 가져가는 연습이 필요합니다.",
                84.0
        );
        reportRepository.save(playerOneReport);

        Report playerTwoReport = Report.builder()
                .maxSpeed(119.4)
                .averageRallyCount(4.9)
                .maxRallyCount(11)
                .minRallyCount(1)
                .totalShotCount(79)
                .firstServeSuccessRate(61.8)
                .secondServeSuccessRate(76.4)
                .firstServeRate(66.9)
                .target(ReportTarget.PLAYER_TWO)
                .build();
        playerTwoReport.assignMatchVideo(matchVideo);
        MotionAnalysis.create(
                playerTwoReport,
                scoringStrokeUrl,
                ShotType.BACKHAND,
                69.7,
                38.3,
                53.6,
                "백핸드 수비 전환 시 체중이 뒤에 남아 있어 스텝 진입과 팔로스루 안정화가 필요합니다.",
                82.0
        );
        reportRepository.save(playerTwoReport);

        Report pipelinePlayerOneReport = Report.builder()
                .maxSpeed(132.4)
                .averageRallyCount(6.4)
                .maxRallyCount(18)
                .minRallyCount(2)
                .totalShotCount(142)
                .firstServeSuccessRate(72.5)
                .secondServeSuccessRate(81.3)
                .firstServeRate(68.2)
                .target(ReportTarget.PLAYER_ONE)
                .build();
        pipelinePlayerOneReport.assignMatchVideo(pipelineMatchVideo);
        MotionAnalysis.create(
                pipelinePlayerOneReport,
                playerAPipelineUrl,
                ShotType.BACKHAND,
                17.469095503163288,
                10.299102099081978,
                2.0791317219047585,
                11.7,
                "5.8°나 초과됐어요. 회전 범위를 크게 줄여야 해요.",
                6.7,
                "3.6°나 초과됐어요. 회전 범위를 크게 줄여야 해요.",
                11.1,
                "9.0° 부족해요. 허리를 더 틀어보세요.",
                "허리 회전을 교정하면 점수가 약 +27점 향상될 수 있어요",
                38.6
        );
        reportRepository.save(pipelinePlayerOneReport);

        Report pipelinePlayerTwoReport = Report.builder()
                .maxSpeed(126.8)
                .averageRallyCount(6.4)
                .maxRallyCount(18)
                .minRallyCount(2)
                .totalShotCount(142)
                .firstServeSuccessRate(66.8)
                .secondServeSuccessRate(78.9)
                .firstServeRate(63.7)
                .target(ReportTarget.PLAYER_TWO)
                .build();
        pipelinePlayerTwoReport.assignMatchVideo(pipelineMatchVideo);
        MotionAnalysis.create(
                pipelinePlayerTwoReport,
                playerBPipelineUrl,
                ShotType.FOREHAND,
                12.441077072115291,
                5.6980761579279715,
                7.193470055295535,
                15.9,
                "3.5° 부족해요. 어깨를 더 틀어보세요.",
                10.4,
                "4.7° 부족해요. 상체를 더 틀어보세요.",
                9.8,
                "2.6° 부족해요. 허리를 더 틀어보세요.",
                "척추 회전을 교정하면 점수가 약 +15점 향상될 수 있어요",
                68.9
        );
        MotionAnalysis.create(
                pipelinePlayerTwoReport,
                playerBBackhandUrl,
                ShotType.BACKHAND,
                12.1,
                7.0,
                10.8,
                "백핸드 임팩트 구간에서 어깨와 허리 회전이 안정적으로 맞아 좋은 자세를 유지하고 있어요.",
                91.4
        );
        reportRepository.save(pipelinePlayerTwoReport);

        highlightVideoRepository.saveAll(List.of(
                HighlightVideo.create(playerOneReport, HighlightVideoType.WINNING_SHOT, playerABestUrl),
                HighlightVideo.create(playerOneReport, HighlightVideoType.WINNING_SHOT, playerABestUrl2),
                HighlightVideo.create(playerOneReport, HighlightVideoType.WINNING_SHOT, playerABestUrl3),
                HighlightVideo.create(playerOneReport, HighlightVideoType.WORST_SHOT, playerAWorstUrl),
                HighlightVideo.create(playerTwoReport, HighlightVideoType.WINNING_SHOT, playerBBestUrl),
                HighlightVideo.create(playerTwoReport, HighlightVideoType.WORST_SHOT, playerBWorstUrl),
                HighlightVideo.create(pipelinePlayerOneReport, HighlightVideoType.WINNING_SHOT, report2PlayerAWinUrl1),
                HighlightVideo.create(pipelinePlayerOneReport, HighlightVideoType.WINNING_SHOT, report2PlayerAWinUrl2),
                HighlightVideo.create(pipelinePlayerOneReport, HighlightVideoType.WINNING_SHOT, report2PlayerAWinUrl3),
                HighlightVideo.create(pipelinePlayerOneReport, HighlightVideoType.WORST_SHOT, report2PlayerAWorstUrl1),
                HighlightVideo.create(pipelinePlayerOneReport, HighlightVideoType.WORST_SHOT, report2PlayerAWorstUrl2),
                HighlightVideo.create(pipelinePlayerTwoReport, HighlightVideoType.WINNING_SHOT, report2PlayerBWinUrl1),
                HighlightVideo.create(pipelinePlayerTwoReport, HighlightVideoType.WINNING_SHOT, report2PlayerBWinUrl2),
                HighlightVideo.create(pipelinePlayerTwoReport, HighlightVideoType.WINNING_SHOT, report2PlayerBWinUrl3),
                HighlightVideo.create(pipelinePlayerTwoReport, HighlightVideoType.WORST_SHOT, report2PlayerBWorstUrl1),
                HighlightVideo.create(pipelinePlayerTwoReport, HighlightVideoType.WORST_SHOT, report2PlayerBWorstUrl2)
        ));
    }

    private MatchVideo getOrCreateMatchVideo(String videoUrl) {
        return matchVideoRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    Court court = courtRepository.findAll().getFirst();
                    MatchVideo matchVideo = MatchVideo.builder()
                            .videoUrl(videoUrl)
                            .matchDate(LocalDate.of(2026, 5, 16))
                            .startTime(LocalTime.of(14, 0))
                            .endTime(LocalTime.of(15, 20))
                            .matchType(MatchType.SINGLES)
                            .reportRequested(true)
                            .build();
                    matchVideo.assignCourt(court);
                    return matchVideoRepository.save(matchVideo);
                });
    }

    private MatchVideo getOrCreatePipelineMatchVideo(String videoUrl) {
        return matchVideoRepository.findAll().stream()
                .filter(matchVideo -> matchVideo.getId() != null && matchVideo.getId() == 2L)
                .findFirst()
                .orElseGet(() -> {
                    Court court = courtRepository.findAll().getFirst();
                    MatchVideo matchVideo = MatchVideo.builder()
                            .videoUrl(videoUrl)
                            .matchDate(LocalDate.of(2026, 5, 5))
                            .startTime(LocalTime.of(10, 0))
                            .endTime(LocalTime.of(12, 0))
                            .matchType(MatchType.SINGLES)
                            .reportRequested(true)
                            .build();
                    matchVideo.assignCourt(court);
                    return matchVideoRepository.save(matchVideo);
                });
    }
}
