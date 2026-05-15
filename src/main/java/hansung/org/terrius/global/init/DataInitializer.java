package hansung.org.terrius.global.init;

import hansung.org.terrius.domain.stadium.entity.Stadium;
import hansung.org.terrius.domain.stadium.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final StadiumRepository stadiumRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (stadiumRepository.count() > 0) {
            return;
        }

        stadiumRepository.saveAll(List.of(
                Stadium.builder()
                        .name("테리우스 잠실 테니스파크")
                        .imageUrl("https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/tennis_1.jpg")
                        .province("서울특별시")
                        .city("송파구")
                        .address("서울특별시 송파구 올림픽로 25 테리우스 잠실 테니스파크")
                        .build(),
                Stadium.builder()
                        .name("테리우스 수원 실내테니스장")
                        .imageUrl("https://terrius-bucket.s3.ap-northeast-2.amazonaws.com/tennis_3.png")
                        .province("경기도")
                        .city("수원시 팔달구")
                        .address("경기도 수원시 팔달구 효원로 241 테리우스 수원 실내테니스장")
                        .build()
        ));
    }
}
