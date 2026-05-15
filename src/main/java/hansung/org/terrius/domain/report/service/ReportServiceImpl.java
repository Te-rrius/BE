package hansung.org.terrius.domain.report.service;

import hansung.org.terrius.domain.match.repository.MatchVideoRepository;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.report.entity.ReportOwnership;
import hansung.org.terrius.domain.report.exception.ReportErrorCode;
import hansung.org.terrius.domain.report.exception.ReportException;
import hansung.org.terrius.domain.report.repository.ReportOwnershipRepository;
import hansung.org.terrius.domain.report.repository.ReportRepository;
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
}
