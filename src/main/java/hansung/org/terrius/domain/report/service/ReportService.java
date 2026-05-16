package hansung.org.terrius.domain.report.service;

import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ReportSortType;
import hansung.org.terrius.domain.report.web.dto.MyReportRes;
import hansung.org.terrius.domain.report.web.dto.ReportDetailRes;

import java.util.List;

public interface ReportService {

    void downloadReports(Long userId, Long matchVideoId);

    List<MyReportRes> getMyReports(Long userId, ReportSortType sort);

    ReportDetailRes getReportDetail(Long userId, Long matchVideoId, ReportTarget target);
}
