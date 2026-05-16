package hansung.org.terrius.domain.report.service;

import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.ReportOwnership;
import hansung.org.terrius.domain.report.entity.enums.ReportTarget;
import hansung.org.terrius.domain.report.entity.enums.ReportSortType;
import hansung.org.terrius.domain.report.exception.ReportErrorCode;
import hansung.org.terrius.domain.report.exception.ReportException;
import hansung.org.terrius.domain.report.repository.ReportOwnershipRepository;
import hansung.org.terrius.domain.report.repository.ReportRepository;
import hansung.org.terrius.domain.report.web.dto.MyReportRes;
import hansung.org.terrius.domain.report.web.dto.ReportDetailRes;
import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.domain.user.exception.UserErrorCode;
import hansung.org.terrius.domain.user.exception.UserException;
import hansung.org.terrius.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final MatchVideoRepository matchVideoRepository;
    private final ReportRepository reportRepository;
    private final ReportOwnershipRepository reportOwnershipRepository;

    @Override
    @Transactional
    public void downloadReports(Long userId, Long matchVideoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!matchVideoRepository.existsById(matchVideoId)) {
            throw new ReportException(ReportErrorCode.MATCH_VIDEO_NOT_FOUND);
        }

        List<Report> reports = reportRepository.findAllByMatchVideoId(matchVideoId);
        if (reports.isEmpty()) {
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }

        List<ReportOwnership> reportOwnerships = reports.stream()
                .filter(report -> !reportOwnershipRepository.existsByUserIdAndReportId(userId, report.getId()))
                .map(report -> ReportOwnership.create(user, report))
                .toList();

        reportOwnershipRepository.saveAll(reportOwnerships);
    }

    @Override
    public List<MyReportRes> getMyReports(Long userId, ReportSortType sort) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        List<ReportOwnership> reportOwnerships = sort == ReportSortType.OLDEST
                ? reportOwnershipRepository.findAllByUserIdOrderByOldest(userId)
                : reportOwnershipRepository.findAllByUserIdOrderByLatest(userId);

        return reportOwnerships.stream()
                .map(MyReportRes::from)
                .toList();
    }

    @Override
    public ReportDetailRes getReportDetail(Long userId, Long matchVideoId, ReportTarget target) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        Report report = reportRepository.findDetailByMatchVideoIdAndTarget(matchVideoId, target)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        if (!reportOwnershipRepository.existsByUserIdAndReportId(userId, report.getId())) {
            throw new ReportException(ReportErrorCode.REPORT_ACCESS_DENIED);
        }

        return ReportDetailRes.from(report);
    }
}
