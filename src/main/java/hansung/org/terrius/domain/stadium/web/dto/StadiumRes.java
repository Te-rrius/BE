package hansung.org.terrius.domain.stadium.web.dto;

import hansung.org.terrius.domain.stadium.entity.Stadium;
import lombok.Builder;

@Builder
public record StadiumRes(
        String name,
        String imageUrl,
        String province,
        String city,
        String address
) {
    public static StadiumRes from(Stadium stadium) {
        return StadiumRes.builder()
                .name(stadium.getName())
                .imageUrl(stadium.getImageUrl())
                .province(stadium.getProvince())
                .city(stadium.getCity())
                .address(stadium.getAddress())
                .build();
    }
}
