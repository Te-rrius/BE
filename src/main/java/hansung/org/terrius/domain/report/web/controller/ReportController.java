package hansung.org.terrius.domain.report.web.controller;

import hansung.org.terrius.domain.report.service.ReportService;
import hansung.org.terrius.global.jwt.CustomUserDetails;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
