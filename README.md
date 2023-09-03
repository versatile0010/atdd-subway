# atdd-subway
---
### ⚒️ week2-step1 PR

이해한 내용을 바탕으로 여러.. 시행착오들을 거쳐서 구현을 시도해보았고..
드디어 작성해둔 테스트가 통과되는 것 같아서.. PR 을 올립니다..! 😥😥

- **💡 구간 추가 시**
  - 상행 종점 부분에 구간을 추가할 수도 있고
    - 예를 들어, [서초->강남] 노선이 존재하면 [건입->서초] 구간을 추가하여 [건입->서초->강남] 으로 노선을 갱신할 수 있다.
  - 하행 종점 부분에 구간을 추가할 수도 있고
    - 예를 들어, [서초->강남] 노선이 존재하면 [강남->건입] 구간을 추가하여 [서초->강남->건입] 으로 노선을 갱신할 수 있다.
  - 사이 부분에 구간을 추가할 수도 있다.
    - 예를 들어, [서초->강남->건입] 노선이 존재하면 [강남->성수] 구간을 추가하여 [서초->강남->성수->건입] 으로 노선으로 갱신할 수 있다.
  - 단 기존 구간보다 크거나 같은 길이의 구간을 추가할 수는 없다.

위 구현을 위해 아래와 같이 `add(Section section)` 을 구현하였습니다.
```agsl
    public void add(Section section) {
        if (sections.isEmpty()) { // 최초로 삽입되는 구간인경우
            sections.add(section);
            return;
        }
        List<Station> stations = getStations(); // 1. 현재 지하철 노선의 정렬된 역 목록을 가져와서
        validateAddSection(stations, section); // 2. 해당 section 을 추가할 수 있는 지 검증
        if (isSectionLast(section, stations)) { // case A. 가장 끝에 추가되어야 하는 경우
            addSectionAtLast(section);
            return;
        }
        if (isSectionFirst(section, stations)) { // case B. 가장 앞에 추가되어야 하는 경우
            addSectionAtFirst(section);
            return;
        }
        addSectionAtBetween(section); // case C. 사이에 끼워넣어져야 하는 경우
    }
```

여기서, 빈 노선이면 isEmpty() == true 이므로 구간을 검증없이 추가합니다.
1. 현재 노선의 정렬된 역 목록을 가져와서
2. section 을 추가할 수 있는 지 검증합니다.
3. 가장 끝에 추가되어야 하는 경우이면 해당 케이스에 맞는 addSectionAtLast 메서드를 호출합니다.
4. 가장 앞에 추가되어야 하는 경우이면 해당 케이스에 맞는 addSectionAtFirst 메서드를 호출합니다.
5. 외에는 사이에 추가되어야 하는 경우이므로 addSectionAtBetween 메서드를 호출합니다.

각각을 차례대로 살펴보려고 합니다!

그 전에, 각 단계에서 사용되는 공통 메서드는 아래와 같습니다.

```agsl
    private Optional<Section> findFirstSectionByUpStation(Station station) {
        // station 을 상행역으로 가지는 구간 중 가장 상행인 구간을 반환한다.
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }
```
이는 입력으로 들어온 station 을 상행역으로 가지는 구간들 중, 가장 상행인 구간을 탐색합니다.
예를 들어, [A-B-C-D] 와 같이 노선이 형성되어 있고 입력으로 B 를 호출하면,
구간 [A-B] [B-C] [C-D] 중, [B-C] 가 Optional 으로 감싸져서 반환될 것 입니다.

## (1) 현재 노선이 정렬된 역 목록을 가져오기
```agsl
    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Section firstSection = sections.get(HEAD);
        Station upStation = firstSection.getUpStation();

        stations.add(upStation); // 상행 종점역은 리스트에 바로 추가한다.
        Optional<Section> section = findFirstSectionByUpStation(upStation);
        // 첫 번째 구간의 상행역을 상행역으로 가지는 구간을 가져온다.
        // 최초에는 firstSection 이 얻어진다.
        updateStationsRecursive(stations, section); // 재귀적으로 탐색하며 리스트에 추가한다.

        return stations;
    }
```
- 현재 구간의 가장 첫 번째 구간(가장 상행)을 가져옵니다.
  - getStations 가 호출되는 환경은, sections 가 empty 이지 않은 경우임을 로직 상 보장됩니다.
  - 이후 첫 번째 구간의 상행역을 가져와서 list 에 추가합니다.
- 그리고 해당 구간의 상행역(상행 종점역)을, 상행역으로 가지는 가장 상행인 구간을 찾습니다.
  - 최초에는 firstSection 과 동일한 객체가 얻어질 것으로 기대됩니다!
- 이후 해당 객체와 list 를 updateStationRecursive 에 전달하여 재귀적으로 list 를 업데이트 합니다.

```agsl
    private void updateStationsRecursive(List<Station> stations, Optional<Section> section) {
        if (section.isEmpty()) {
            return; // terminate condition 은 더이상 탐색할 section 이 존재하지 않는 경우이다.
        }
        Station downStation = section.get().getDownStation(); // 해당 구간의 하행역을 가져와서
        stations.add(downStation); // 리스트에 추가한 뒤
        // 해당 구간의 하행역을 상행역으로 가지는 다음 구간을 재귀적으로 탐색한다.
        updateStationsRecursive(stations, findFirstSectionByUpStation(downStation));
    }
```
- 탐색할 section 이 null 이면 해당 재귀함수를 terminate 합니다.
- section 이 존재하면, 해당 section 의 downStation 을 list 에 추가합니다.
- 그리고 해당 downStation 을 상행역으로 가지는 가장 상행인 구간을 파라미터로 재귀함수를 호출합니다.

## 2. section 을 추가할 수 있는 지 검증하기.
- 새로이 추가할 구간이 들어오면, 해당 노선에 추가할 수 있는지 기본적인 검증을 수행합니다.
```agsl
    private void validateAddSection(List<Station> stations, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        boolean isUpStationExists = false;
        boolean isDownStationExists = false;
        for (Station station : stations) {
            if (station.equals(upStation)) {
                isUpStationExists = true;
            }
            if (station.equals(downStation)) {
                isDownStationExists = true;
            }
            if (isUpStationExists && isDownStationExists) {
                throw new AlreadyExistStationsException();
            }
        }
        if (!isUpStationExists && !isDownStationExists) {
            throw new InvalidAddSectionsException();
        }
    }
```
- 새롭게 추가할 상행역과 하행역을 U, D 라고 약속했을 때,
  - U,D 가 모두 노선에 존재하거나
  - U,D 모두 노선에 존재하지 않으면 구간추가예외를 터뜨립니다.

## (3) 가장 끝(하행 종점 부분)에 추가되어야 하는 경우에 대한 처리
- 가장 끝(하행 종점 부분)에 추가되어야 하는 구간인 지 판단하는 메서드는 아래와 같습니다.
```agsl
    private boolean isSectionLast(Section section, List<Station> stations) {
        Station upStation = section.getUpStation();
        Station finalStation = stations.get(stations.size() - 1);
        return finalStation.equals(upStation); // 삽입할 구간의 상행역과 하행 종점역이 같으면 마지막에 추가되어야 하는 CASE
    }
```
- 추가해야 할 구간의 상행역과, 하행종점역이 같으면 하행종점부분에 추가해야 하는 경우로 판단합니다.
  - 예를 들어, [서초 -> 강남 -> 건입] 노선이 존재할 때 하행종점역은 `건입` 이고
    - [강남 -> 건입] 구간 생성 요청이 들어왔을 때, 상행역은 `강남` 입니다.
      - 이 경우에는 하행종점부분에 추가해야 할 경우가 아니라고 판단합니다.
    - 반면 [건입 -> 성수] 구간 생성 요청이 들어오면, 상행역은 `건입` 입니다.
      - 현재 노선의 하행종점역 또한 `건입` 이므로, 두 Station 은 일치합니다.
        - 그러면 하행 종점 부분에 추가해야 할 구간으로 판단하여 아래와 같이 노선이 갱신되도록 처리합니다.
        - [서초 -> 강남 -> 건입 -> 성수]
    - 이때, Station 의 일치를 판단하는 로직은 equals 를 override 하여 구현하였습니다.
      - Station 엔티티의 ID 는 Auto-generated 되는 고유한 PK 이므로, PK 를 통해서 비교하도록 구현하였습니다.
```agsl
    @Override
    public boolean equals(Object o) {
        Station station = (Station) o;
        return Objects.equals(id, station.getId());
    }
```
그리고 실제로 구간을 하행 종점 부분에 추가하는 메서드는 아래와 같습니다.
```agsl
    private void addSectionAtLast(Section section) {
        Station upStation = section.getUpStation(); // 추가할 구간의 상행역과
        int tail = sections.size() - 1;
        Section lastSection = sections.get(tail); // 가장 마지막 구간을 가져온다
        Station downStationOfLastSection = lastSection.getDownStation(); // 하행 종점역을 가져온다.
        // 하행 종점역과 추가할 구간의 상행역이 다르면 추가할 수 없다.
        if (!downStationOfLastSection.equals(upStation)) {
            throw new InvalidAddLastSectionException();
        }
        // 외의 경우에는 추가한다.
        sections.add(tail + 1, section);
    }
```

## (4) 가장 앞(상행 종점 부분)에 추가되어야 하는 경우에 대한 처리
- 이는 (3) 번 케이스에서 상행역 <-> 하행역을 바꿔주어 거의 동일한 로직으로 구현할 수 있었습니다.
- 해당 경우에 속하는 상황은 아래와 같습니다.
  - [서초 - 강남] 노선이 존재할 때, [건입 - 서초] 구간 생성 요청이 들어오면 [건입 - 서초 - 강남] 으로 갱신되어야 함.

## (5) 기존 구간들 사이에 추가되어야 하는 경우에 대한 처리 😱
- [A-B] [B-C] [C-D] 노선이 존재한다고 가정하고, [B-X] 구간을 추가하려고 합니다.
- 목표 상태는 [A-B] [B-X] [X-C] [C-D] 일 것입니다.
- 이를 위해 수정해야 할 것은 [B-C] 이며 (이를 oldSection 으로 부르겠습니다.)
- 추가해야할 것은 [B-X] 입니다. (oldSection 이 위치한 자리에 추가해야 합니다.)

이를 만족하기 위해,
- oldSection 을 찾고
- distance 를 검증한 다음
- oldSection 이 sections 내에서 존재했던 index 를 찾아낸 다음, 해당 index 에 새로운 구간을 추가합니다.
- 그리고 oldSection 의 상행역을, 새로운 구간의 하행역으로 수정합니다. (거리 또한 갱신해줍니다.)

이를 구현한 코드는 아래와 같습니다.
```agsl
    private void addSectionAtBetween(Section newSection) {
        // 추가할 구간의 상행역을, 상행역으로 가지고 있는 가장 처음 구간(let oldSection)을 가져온다.
        Section oldSection = findFirstSectionByUpStation(newSection.getUpStation()).orElseThrow(NotFoundSectionException::new);
        // 기존 구간보다 크거나 같은 거리의 새로운 구간을 사이에 추가할 수 없다.
        validateAddSectionDistance(oldSection, newSection);
        // 검증을 통과한다면 추가한다.
        int indexOfOldSection = IntStream.range(0, sections.size()) // oldSection 의 순서를 찾아낸다.
                .filter(index -> sections.get(index)
                        .equals(oldSection))
                .boxed().findFirst().orElseThrow(NotFoundSectionException::new);
        sections.add(indexOfOldSection, newSection); // oldSection 자리에 새로운 구간을 추가하고 oldSection 을 적절히 수정한다.
        oldSection.updateUpStation(newSection.getDownStation(), oldSection.getDistance() - newSection.getDistance());
    }
```

## ✅ MockTest 를 통해 Section 추가가 올바르게 이루어지는 지 확인해보았습니다.
`assertThatThrownBy()` 를 적극적으로 활용하였습니다.

1. 상행 종점 구간을 추가하는 경우
```agsl
    @DisplayName("상행 종점 구간을 추가할 수 있다.")
    @Test
    void addSectionAtFirst() {
        // given  (서초역-강남역) 노선에 (건대입구역-서초역) 구간을 상행종점 구간으로 등록할 수 있다.
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서초역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(건대입구역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(2L, 3L, 10L);
        CreateSectionResponse response = lineService.addSection(request, 1L);
        // then
        assertThat(response.getStations()).hasSize(3)
                .extracting("name")
                .containsExactly("건대입구역", "서초역", "강남역");
    }
```

2. 사이에 구간을 끼워넣는 경우
```agsl
    @DisplayName("기존에 존재하는 노선 사이에 새로운 구간을 추가할 수 있다.")
    @Test
    void addSectionAtBetweenCase1() {
        // given (서초--2->강남--4->건대 입구) 노선에 (강남--1->성수) 구간을 등록할 수 있다.
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(성수역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);

        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(4L, 1L, 1L);
        CreateSectionResponse response = lineService.addSection(request, 1L);

        // then
        assertThat(response.getStations()).hasSize(4)
                .extracting("name")
                .containsExactly("서초역", "강남역", "성수역", "건대입구역");
    }
```
3. 하행 종점 구간을 추가하는 경우
```agsl
    @DisplayName("하행 종점 구간을 추가할 수 있다.")
    @Test
    void addSectionAtBetweenCase3() {
        // given (서초--2->강남--4->건대 입구) 노선에 (건대 입구역 -> 성수역) 구간을 등록할 수 있다.
        when(stationRepository.findById(3L)).thenReturn(Optional.of(건대입구역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(성수역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);

        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(4L, 3L, 1L);
        CreateSectionResponse response = lineService.addSection(request, 1L);

        // then
        assertThat(response.getStations()).hasSize(4)
                .extracting("name")
                .containsExactly("서초역", "강남역", "건대입구역", "성수역");
    }
```

## ✅ 인수 테스트를 통해 Section 을 추가할 때 발생하는 쿼리들을 확인해보았습니다.
### 1. (서초역 -> 강남역) 노선에 상행 종점 구간 (건대입구역->서초역)을 추가하는 경우
```agsl
HTTP/1.1 201 
Location: /lines/1/sections
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 03 Sep 2023 05:09:54 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "id": 1,
    "stations": [
        {
            "id": 3,
            "name": "건대입구역"
        },
        {
            "id": 2,
            "name": "서초역"
        },
        {
            "id": 1,
            "name": "강남역"
        }
    ]
}
```
위와 같은 구간 생성 시나리오에서 6 개의 쿼리가 발생하였습니다. (노선 조회 1회, 상행역 하행역 조회 각각 2회, section 조회 1회, station 조회 1회, section 저장 1회)
- ` select l1_0.id,l1_0.color,l1_0.created_date,l1_0.distance,l1_0.modified_date,l1_0.name,l1_0.head from line l1_0 where l1_0.id=1; `
-  `select s1_0.id,s1_0.created_date,s1_0.modified_date,s1_0.name from station s1_0 where s1_0.id=2;`
-  `select s1_0.id,s1_0.created_date,s1_0.modified_date,s1_0.name from station s1_0 where s1_0.id=3;`
-  `select s1_0.line_id,s1_0.id,s1_0.created_date,s1_0.distance,s1_0.down_station_id,s1_0.modified_date,s1_0.up_station_id from section s1_0 where array_contains('ar2: ARRAY [CAST(1 AS BIGINT), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL]',s1_0.line_id);`
   - 이 .. NULL 배열의 정체는 무엇일까요?? 😥😱 찾아보고 있는데... 아시는 분 있으시면 알려주시면 감사드려요 ㅜ_ㅜ 
-  `select s1_0.id,s1_0.created_date,s1_0.modified_date,s1_0.name from station s1_0 where s1_0.id=1;`
   - 이 쿼리가 발생하는 이유를 찾는 중입니다 ㅜ_ㅜ 왜지..?
-  `insert into section (created_date,distance,down_station_id,line_id,modified_date,up_station_id,id) values ('2023-09-03T14:09:54.860+0900',1,2,1,'2023-09-03T14:09:54.860+0900',3,default);`

### 2. (서초역 -> 강남역) 노선에 (강남역 -> 건대입구역) 을 추가하는 경우
```agsl
HTTP/1.1 201 
Location: /lines/1/sections
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 03 Sep 2023 05:16:50 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "id": 1,
    "stations": [
        {
            "id": 2,
            "name": "서초역"
        },
        {
            "id": 1,
            "name": "강남역"
        },
        {
            "id": 3,
            "name": "건대입구역"
        }
    ]
}
```
- 발생 쿼리는 이전 경우와 동일하게 조회 5번, section 저장 1회 발생합니다.

### 3. (서초역 -> 강남역 -> 건대입구역) 노선에 (강남역 -> 성수역) 구간을 추가하는 경우
```agsl
HTTP/1.1 201 
Location: /lines/1/sections
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 03 Sep 2023 05:19:55 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "id": 1,
    "stations": [
        {
            "id": 2,
            "name": "서초역"
        },
        {
            "id": 1,
            "name": "강남역"
        },
        {
            "id": 4,
            "name": "성수역"
        },
        {
            "id": 3,
            "name": "건대입구역"
        }
    ]
}
```

사이 구간에 추가하는 경우에는 조회 5회, 저장 1회 그리고 업데이트 1회으로 총 7회의 쿼리가 발생했습니다.
업데이트 쿼리는 oldSection 을 갱신해주는 과정에서 발생한 것으로 사료됩니다!!

` update section set created_date='2023-09-03T14:19:55.579+0900',distance=9,down_station_id=3,line_id=1,modified_date='2023-09-03T14:19:55.633+0900',up_station_id=4 where id=2; `