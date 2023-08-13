package kuit.subway.controller;

import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.response.CreateStationResponse;
import kuit.subway.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stations")
@RestController
public class StationController {
    private final StationService stationService;

    @PostMapping
    public ResponseEntity<CreateStationResponse> createStation(
            @RequestBody CreateStationRequest request
    ) {
        String name = request.getName();
        log.info("[지하철 " + name + " 역 생성 API 를 호출합니다.]");
        CreateStationResponse response = stationService.createOne(name);
        return ResponseEntity.ok(response);
    }
}
