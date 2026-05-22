package hansung.org.terrius.domain.stadium.web.controller;

import hansung.org.terrius.domain.stadium.service.StadiumService;
import hansung.org.terrius.domain.stadium.web.dto.CalendarDateRes;
import hansung.org.terrius.domain.stadium.web.dto.MatchVideoRes;
import hansung.org.terrius.domain.stadium.web.dto.ReportRequestRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumDetailRes;
import hansung.org.terrius.domain.stadium.web.dto.StadiumRes;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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

    @GetMapping("/{stadiumId}")
    public ResponseEntity<SuccessResponse<StadiumDetailRes>> getStadiumCourts(
            @PathVariable Long stadiumId
    ) {
        StadiumDetailRes res = stadiumService.getStadiumCourts(stadiumId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(res));
    }

    @GetMapping("/{stadiumId}/report-downloads/dates")
    public ResponseEntity<SuccessResponse<List<CalendarDateRes>>> getReportDownloadDates(
            @PathVariable Long stadiumId
    ) {
        List<CalendarDateRes> res = stadiumService.getReportDownloadDates(stadiumId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(res));
    }

    @GetMapping("/{stadiumId}/report-downloads/times")
    public ResponseEntity<SuccessResponse<List<MatchVideoRes>>> getReportDownloadTimes(
            @PathVariable Long stadiumId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer courtNumber
    ) {
        List<MatchVideoRes> res = stadiumService.getReportDownloadTimes(stadiumId, date, courtNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(res));
    }

    @GetMapping("/{stadiumId}/report-requests")
    public ResponseEntity<SuccessResponse<ReportRequestRes>> getReportRequests(
            @PathVariable Long stadiumId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer courtNumber
    ) {
        ReportRequestRes res = stadiumService.getReportRequests(stadiumId, date, courtNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(res));
    }

    @PostMapping("/{stadiumId}/report-requests/{matchVideoId}")
    public ResponseEntity<SuccessResponse<?>> requestReport(
            @PathVariable Long stadiumId,
            @PathVariable Long matchVideoId
    ) {
        stadiumService.requestReport(stadiumId, matchVideoId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.created(null));
    }
}
