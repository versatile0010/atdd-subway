package kuit.subway.controller;

import jakarta.validation.Valid;
import kuit.subway.dto.request.*;
import kuit.subway.dto.response.*;
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
        LineInfoResponse response = lineService.getLineDetails(id);
        return ResponseEntity.ok(response);
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

    @PostMapping("/{id}/sections")
    public ResponseEntity<CreateSectionResponse> createSection(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateSectionRequest request) {
        log.info("노선{id=" + id + "} 구간 추가 API 를 호출합니다.");
        CreateSectionResponse response = lineService.addSection(request, id);
        return ResponseEntity.created(
                URI.create("/lines/" + id + "/sections")
        ).body(response);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<DeleteSectionResponse> deleteSection(
            @PathVariable("id") Long id,
            @RequestBody DeleteSectionRequest request) {
        log.info("노선{id=" + id + "} 구간 삭제 API 를 호출합니다.");
        DeleteSectionResponse response = lineService.removeSection(request, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    @DeleteMapping("/{id}/v2/sections")
    public ResponseEntity<DeleteSectionResponse> deleteSectionV2(
            @PathVariable("id") Long id,
            @RequestBody DeleteSectionRequestV2 request) {
        log.info("노선{id=" + id + "} 구간 삭제 V2 API 를 호출합니다.");
        DeleteSectionResponse response = lineService.removeSectionV2(request, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
