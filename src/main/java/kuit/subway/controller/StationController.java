package kuit.subway.controller;

import jakarta.validation.Valid;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.response.CreateStationResponse;
import kuit.subway.dto.response.DeleteStationResponse;
import kuit.subway.dto.response.StationListResponse;
import kuit.subway.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stations")
@RestController
public class StationController {
    private final StationService stationService;

    @PostMapping
    public ResponseEntity<CreateStationResponse> createStation(
            @RequestBody @Valid CreateStationRequest request
    ) {
        String name = request.getName();
        log.info("[지하철 " + name + " 역 생성 API 를 호출합니다.]");
        CreateStationResponse response = stationService.createOne(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<StationListResponse> getStations() {
        log.info("[지하철 목록 조회 API 를 호출합니다.]");
        StationListResponse response = stationService.getStations();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteStationResponse> deleteStation(@PathVariable("id") Long id) {
        log.info("[지하철 삭제(id=" + id + ") API 를 호출합니다.");
        Long deletedId = stationService.deleteStation(id);
        DeleteStationResponse response = new DeleteStationResponse(deletedId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
