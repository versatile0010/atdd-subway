package kuit.subway.controller;

import kuit.subway.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/stations")
@RestController
public class StationController {
    private final StationService stationService;
}
