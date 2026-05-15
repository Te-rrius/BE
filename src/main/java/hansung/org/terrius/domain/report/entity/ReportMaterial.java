package hansung.org.terrius.domain.report.entity;

import hansung.org.terrius.domain.report.entity.enums.ReportMaterialType;
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
@Table(name = "report_materials")
public class ReportMaterial extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자료가 속한 리포트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    // 리포트 자료 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private ReportMaterialType materialType;

    // S3 등에 저장된 영상 URL
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    public static ReportMaterial create(Report report, ReportMaterialType materialType, String videoUrl) {
        ReportMaterial reportMaterial = ReportMaterial.builder()
                .report(report)
                .materialType(materialType)
                .videoUrl(videoUrl)
                .build();

        report.addReportMaterial(reportMaterial);

        return reportMaterial;
    }
}
