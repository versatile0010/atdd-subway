package kuit.subway.controller;

import jakarta.validation.Valid;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.response.CreateLineResponse;
import kuit.subway.service.LineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    @PostMapping
    public ResponseEntity<CreateLineResponse> createLine(
            @RequestBody @Valid CreateLineRequest request) {
        log.info("노선 생성 API 를 호출합니다.");
        CreateLineResponse response = lineService.createOne(request);
        return ResponseEntity.created(
                URI.create("/lines/" + response.getId())).body(response);
    }
}
