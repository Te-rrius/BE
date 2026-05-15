package hansung.org.terrius.domain.stadium.web.controller;

import hansung.org.terrius.domain.stadium.service.StadiumService;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<StadiumRes>>> getStadiums(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name
    ) {
        List<StadiumRes> res = stadiumService.getStadiums(province, city, name);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }
}
