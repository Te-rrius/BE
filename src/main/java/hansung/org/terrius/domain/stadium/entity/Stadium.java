package hansung.org.terrius.domain.stadium.entity;

import hansung.org.terrius.domain.match.entity.MatchVideo;
import hansung.org.terrius.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "stadiums")
public class Stadium extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // 시/도
    @Column(name = "province", nullable = false)
    private String province;

    // 시/군/구
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @Builder.Default
    @OneToMany(mappedBy = "stadium")
    private List<MatchVideo> matchVideos = new ArrayList<>();

    public void addMatchVideo(MatchVideo matchVideo) {
        this.matchVideos.add(matchVideo);
    }
}
