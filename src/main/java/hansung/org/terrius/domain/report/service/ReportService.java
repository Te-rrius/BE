package hansung.org.terrius.domain.report.service;

import hansung.org.terrius.domain.report.entity.enums.ReportSortType;
import hansung.org.terrius.domain.report.web.dto.MyReportRes;

import java.util.List;

public interface ReportService {

    void downloadReports(Long userId, Long matchVideoId);

    List<MyReportRes> getMyReports(Long userId, ReportSortType sort);
}
