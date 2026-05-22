package hansung.org.terrius.domain.stadium.web.dto;

import hansung.org.terrius.domain.stadium.entity.Court;

import java.util.List;

public record StadiumDetailRes(
        List<Integer> courtNumbers
) {
    public static StadiumDetailRes from(List<Court> courts) {
        List<Integer> courtNumbers = courts.stream()
                .map(Court::getCourtNumber)
                .toList();
        return new StadiumDetailRes(courtNumbers);
    }
}
