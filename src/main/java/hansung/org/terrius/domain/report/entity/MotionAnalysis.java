package hansung.org.terrius.domain.report.entity;

import hansung.org.terrius.domain.report.entity.enums.ShotType;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "motion_analyses")
public class MotionAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "shot_type", nullable = false)
    private ShotType shotType;

    @Column(name = "shoulder_rotation_angle", nullable = false)
    private Double shoulderRotationAngle;

    @Column(name = "spine_rotation_angle", nullable = false)
    private Double spineRotationAngle;

    @Column(name = "waist_rotation_angle", nullable = false)
    private Double waistRotationAngle;

    @Column(name = "improvement_point", nullable = false)
    private String improvementPoint;

    @Column(name = "score", nullable = false)
    private Double score;

    public static MotionAnalysis create(
            Report report,
            String videoUrl,
            ShotType shotType,
            Double shoulderRotationAngle,
            Double spineRotationAngle,
            Double waistRotationAngle,
            String improvementPoint,
            Double score
    ) {
        MotionAnalysis motionAnalysis = MotionAnalysis.builder()
                .report(report)
                .videoUrl(videoUrl)
                .shotType(shotType)
                .shoulderRotationAngle(shoulderRotationAngle)
                .spineRotationAngle(spineRotationAngle)
                .waistRotationAngle(waistRotationAngle)
                .improvementPoint(improvementPoint)
                .score(score)
                .build();

        report.addMotionAnalysis(motionAnalysis);

        return motionAnalysis;
    }
}
