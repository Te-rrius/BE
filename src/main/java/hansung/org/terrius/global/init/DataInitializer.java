package hansung.org.terrius.global.init;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.match.entity.enums.MatchType;
import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.ReportMaterial;
import hansung.org.terrius.domain.report.entity.enums.ReportMaterialType;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
import hansung.org.terrius.domain.report.repository.ReportMaterialRepository;
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
    private final ReportMaterialRepository reportMaterialRepository;

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
                        .address("서울특별시 송파구 올림픽로 25 테리우스 잠실 테니스파크")
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
        String unforcedErrorUrl = "https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/01_unforced_error.mp4";
        MatchVideo matchVideo = getOrCreateMatchVideo(scoringStrokeUrl);

        if (reportRepository.count() > 0) {
            return;
        }

        Report playerOneReport = Report.builder()
                .shotType(ShotType.FOREHAND)
                .maxSpeed(128.7)
                .shoulderRotationAngle(74.2)
                .spineRotationAngle(41.5)
                .waistRotationAngle(58.8)
                .averageRallyCount(5.6)
                .maxRallyCount(13)
                .minRallyCount(2)
                .totalShotCount(86)
                .improvementPoint("임팩트 직전 상체가 먼저 열리는 경향이 있어 허리 회전 타이밍을 늦추는 연습이 필요합니다.")
                .firstServeSuccessRate(68.5)
                .secondServeSuccessRate(82.1)
                .firstServeRate(71.3)
                .target(ReportTarget.PLAYER_ONE)
                .build();
        playerOneReport.assignMatchVideo(matchVideo);
        reportRepository.save(playerOneReport);

        Report playerTwoReport = Report.builder()
                .shotType(ShotType.BACKHAND)
                .maxSpeed(119.4)
                .shoulderRotationAngle(69.7)
                .spineRotationAngle(38.3)
                .waistRotationAngle(53.6)
                .averageRallyCount(4.9)
                .maxRallyCount(11)
                .minRallyCount(1)
                .totalShotCount(79)
                .improvementPoint("백핸드 수비 전환 시 체중이 뒤에 남아 있어 스텝 진입과 팔로스루 안정화가 필요합니다.")
                .firstServeSuccessRate(61.8)
                .secondServeSuccessRate(76.4)
                .firstServeRate(66.9)
                .target(ReportTarget.PLAYER_TWO)
                .build();
        playerTwoReport.assignMatchVideo(matchVideo);
        reportRepository.save(playerTwoReport);

        reportMaterialRepository.saveAll(List.of(
                ReportMaterial.create(
                        playerOneReport,
                        ReportMaterialType.MOTION,
                        scoringStrokeUrl
                ),
                ReportMaterial.create(
                        playerOneReport,
                        ReportMaterialType.WINNING_SHOT,
                        scoringStrokeUrl
                ),
                ReportMaterial.create(
                        playerOneReport,
                        ReportMaterialType.WORST_SHOT,
                        unforcedErrorUrl
                ),
                ReportMaterial.create(
                        playerTwoReport,
                        ReportMaterialType.MOTION,
                        scoringStrokeUrl
                ),
                ReportMaterial.create(
                        playerTwoReport,
                        ReportMaterialType.WINNING_SHOT,
                        unforcedErrorUrl
                ),
                ReportMaterial.create(
                        playerTwoReport,
                        ReportMaterialType.WORST_SHOT,
                        scoringStrokeUrl
                )
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
}
