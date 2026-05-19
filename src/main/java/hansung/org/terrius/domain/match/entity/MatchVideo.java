package hansung.org.terrius.domain.match.entity;

import hansung.org.terrius.domain.match.entity.enums.MatchType;
import hansung.org.terrius.domain.report.entity.Report;
import hansung.org.terrius.domain.stadium.entity.Court;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_videos")
public class MatchVideo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // S3 등에 저장된 전체 경기 영상 URL
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    // 경기가 진행된 날짜
    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    // 경기 시작 시간
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // 경기 종료 시간
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // 경기 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType matchType;

    // 리포트 신청 여부
    @Builder.Default
    @Column(name = "report_requested", nullable = false)
    private Boolean reportRequested = false;

    @Builder.Default
    @OneToMany(mappedBy = "matchVideo")
    private List<Report> reports = new ArrayList<>();

    // 경기 영상이 촬영된 코트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    // -------------------- 메서드 --------------------
    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void assignCourt(Court court) {
        this.court = court;
        court.addMatchVideo(this);
    }

    public void requestReport() {
        this.reportRequested = true;
    }
}
