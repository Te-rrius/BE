package hansung.org.terrius.domain.report.web.dto;

import hansung.org.terrius.domain.report.entity.ReportMaterial;
import hansung.org.terrius.domain.report.entity.enums.ReportMaterialType;
import lombok.Builder;

@Builder
public record ReportMaterialRes(
        ReportMaterialType materialType,
        String materialTypeName,
        String videoUrl
) {
    public static ReportMaterialRes from(ReportMaterial reportMaterial) {
        return ReportMaterialRes.builder()
                .materialType(reportMaterial.getMaterialType())
                .materialTypeName(reportMaterial.getMaterialType().getDescription())
                .videoUrl(reportMaterial.getVideoUrl())
                .build();
    }
}
