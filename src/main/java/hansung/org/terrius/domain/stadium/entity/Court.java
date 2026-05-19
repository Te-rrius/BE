package hansung.org.terrius.domain.stadium.entity;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "courts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_courts_stadium_id_court_number",
                columnNames = {"stadium_id", "court_number"}
        )
)
public class Court extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구장 내 코트 번호 (1, 2, 3 ...) — 표시용 접미사("구장")는 프론트에서 처리
    @Column(name = "court_number", nullable = false)
    private Integer courtNumber;

    // 코트가 속한 구장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @Builder.Default
    @OneToMany(mappedBy = "court")
    private List<MatchVideo> matchVideos = new ArrayList<>();

    // -------------------- 메서드 --------------------
    public void addMatchVideo(MatchVideo matchVideo) {
        this.matchVideos.add(matchVideo);
    }

    public void assignStadium(Stadium stadium) {
        this.stadium = stadium;
        stadium.addCourt(this);
    }
}
