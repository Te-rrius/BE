package hansung.org.terrius.domain.report.entity;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ShotType;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reports")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 분석 대상이 되는 대표 샷 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "shot_type", nullable = false)
    private ShotType shotType;

    // 경기 또는 분석 구간에서 기록된 최고 타구 속도
    @Column(name = "max_speed", nullable = false)
    private Double maxSpeed;

    // 샷 동작 중 어깨가 회전한 각도
    @Column(name = "shoulder_rotation_angle", nullable = false)
    private Double shoulderRotationAngle;

    // 샷 동작 중 척추 축이 회전한 각도
    @Column(name = "spine_rotation_angle", nullable = false)
    private Double spineRotationAngle;

    // 샷 동작 중 허리가 회전한 각도
    @Column(name = "waist_rotation_angle", nullable = false)
    private Double waistRotationAngle;

    // 경기 전체 랠리의 평균 지속 횟수
    @Column(name = "average_rally_count", nullable = false)
    private Double averageRallyCount;

    // 경기 중 가장 길게 이어진 랠리 횟수
    @Column(name = "max_rally_count", nullable = false)
    private Integer maxRallyCount;

    // 경기 중 가장 짧게 종료된 랠리 횟수
    @Column(name = "min_rally_count", nullable = false)
    private Integer minRallyCount;

    // 분석 구간에서 집계된 전체 샷 수
    @Column(name = "total_shot_count", nullable = false)
    private Integer totalShotCount;

    // 분석 결과 기반의 개선 포인트 설명
    @Column(name = "improvement_point", nullable = false)
    private String improvementPoint;

    // 퍼스트 서브 시도 중 성공한 비율
    @Column(name = "first_serve_success_rate", nullable = false)
    private Double firstServeSuccessRate;

    // 세컨드 서브 시도 중 성공한 비율
    @Column(name = "second_serve_success_rate", nullable = false)
    private Double secondServeSuccessRate;

    // 전체 서브 시도 중 퍼스트 서브가 차지하는 비율
    @Column(name = "first_serve_rate", nullable = false)
    private Double firstServeRate;

    // 단식 경기 기준 두 선수의 리포트를 구분하기 위한 대상
    @Enumerated(EnumType.STRING)
    @Column(name = "target", nullable = false)
    private ReportTarget target;

    // 리포트가 귀속되는 원본 경기 영상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_video_id", nullable = false)
    private MatchVideo matchVideo;

    @Builder.Default
    @OneToMany(mappedBy = "report")
    private List<ReportOwnership> reportOwnerships = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "report")
    private List<ReportMaterial> reportMaterials = new ArrayList<>();

    public void addReportOwnership(ReportOwnership reportOwnership) {
        this.reportOwnerships.add(reportOwnership);
    }

    public void addReportMaterial(ReportMaterial reportMaterial) {
        this.reportMaterials.add(reportMaterial);
    }

    public void assignMatchVideo(MatchVideo matchVideo) {
        this.matchVideo = matchVideo;
        matchVideo.addReport(this);
    }
}
