package kuit.subway.controller;

import jakarta.validation.Valid;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.ModifyLineRequest;
import kuit.subway.dto.response.CreateLineResponse;
import kuit.subway.dto.response.DeleteLineResponse;
import kuit.subway.dto.response.LineInfoResponse;
import kuit.subway.dto.response.ModifyLineResponse;
import kuit.subway.service.LineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<LineInfoResponse> getLineInfo(@PathVariable("id") Long id) {
        log.info("노선{id=" + id + "} 조회 API 를 호출합니다.");
        return ResponseEntity.ok(lineService.getLineDetails(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ModifyLineResponse> modifyLine(
            @PathVariable("id") Long id,
            @RequestBody @Valid ModifyLineRequest request) {
        log.info("노선{id=" + id + "} 수정 API 를 호출합니다.");
        ModifyLineResponse response = lineService.updateLine(request, id);
        return ResponseEntity.created(
                URI.create("/lines/" + response.getId())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteLineResponse> deleteLine(@PathVariable("id") Long id) {
        log.info("노선{id=" + id + "} 삭제 API 를 호출합니다.");
        DeleteLineResponse response = lineService.deleteLine(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

}
