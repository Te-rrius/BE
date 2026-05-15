package hansung.org.terrius.domain.stadium.service;

import hansung.org.terrius.domain.stadium.entity.Stadium;
import hansung.org.terrius.domain.stadium.repository.StadiumRepository;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    @Override
    public List<StadiumRes> getStadiums(String province, String city, String name) {
        List<Stadium> stadiums = stadiumRepository.findAllBySearchCondition(
                normalize(province),
                normalize(city),
                normalize(name)
        );

        return stadiums.stream()
                .map(StadiumRes::from)
                .toList();
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
