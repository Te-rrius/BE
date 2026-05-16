package hansung.org.terrius.domain.report.web.controller;

import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ReportSortType;
import hansung.org.terrius.domain.report.service.ReportService;
import hansung.org.terrius.domain.report.web.dto.MyReportRes;
import hansung.org.terrius.domain.report.web.dto.ReportDetailRes;
import hansung.org.terrius.global.jwt.CustomUserDetails;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/match-videos/{matchVideoId}/download")
    public ResponseEntity<SuccessResponse<?>> downloadReports(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchVideoId
    ) {
        reportService.downloadReports(customUserDetails.getUser().getId(), matchVideoId);
        return ResponseEntity.ok(SuccessResponse.empty());
    }

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<List<MyReportRes>>> getMyReports(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "LATEST") ReportSortType sort
    ) {
        List<MyReportRes> res = reportService.getMyReports(customUserDetails.getUser().getId(), sort);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }

    @GetMapping("/match-videos/{matchVideoId}")
    public ResponseEntity<SuccessResponse<ReportDetailRes>> getReportDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchVideoId,
            @RequestParam ReportTarget target
    ) {
        ReportDetailRes res = reportService.getReportDetail(customUserDetails.getUser().getId(), matchVideoId, target);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }
}
