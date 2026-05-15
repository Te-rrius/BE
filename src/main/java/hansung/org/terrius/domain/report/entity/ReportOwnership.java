package hansung.org.terrius.domain.report.entity;

import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "report_ownerships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_report_ownership_user_report",
                        columnNames = {"user_id", "report_id"}
                )
        }
)
public class ReportOwnership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리포트를 다운받아 소유한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 사용자가 소유한 리포트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public static ReportOwnership create(User user, Report report) {
        ReportOwnership reportOwnership = ReportOwnership.builder()
                .user(user)
                .report(report)
                .build();

        user.addReportOwnership(reportOwnership);
        report.addReportOwnership(reportOwnership);

        return reportOwnership;
    }
}
