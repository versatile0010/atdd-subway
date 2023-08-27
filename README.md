# atdd-subway
## Week2
### Step1. 구간 추가 제약사항 변경

- 추가 케이스 세분화
  - 상행 종점으로 등록할 경우 맨 상위 구간으로
  - 하행 종점으로 등록할 경우 맨 하위 구간으로
  - 사이 구간으로 등록할 경우를 각각 나누어서 적용
- 예외 케이스
  - 구간 request 로 들어온 상행역, 하행역이 해당 노선에 모두 존재하지 않으면 reject
  - 상행역과 하행역 모두 이미 구간에 등록되어 있다면 reject
  - 기존 구간보다 크거나 같은 거리의 구간 생성 요청은 reject
> POST lines/{line-id}/v2/sections 로 매핑
- request body
```
sectionType : Long  // 0: 사이구간, 1:상행 종점, 2: 하행 종점
downStationId : Long
upStationId : Long
```
- response body
```
lineId : Long
```
