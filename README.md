# atdd-subway
## Week2
### Step1. 구간 추가 제약사항 변경

> POST lines/{line-id}/sections 로 매핑
- request body
```
sectionType : Integer  // 0: 사이구간, 1:상행 종점, 2: 하행 종점
downStationId : Long
upStationId : Long
```
- response body
```
lineId : Long
```

- 추가 케이스 세분화
  - 상행 종점으로 등록할 경우 맨 상위 구간으로
  - 하행 종점으로 등록할 경우 맨 하위 구간으로
  - 사이 구간으로 등록할 경우를 각각 나누어서 적용
- 예외 케이스
  - 구간 request 로 들어온 상행역, 하행역이 해당 노선에 모두 존재하지 않으면 reject
  - 상행역과 하행역 모두 이미 구간에 등록되어 있다면 reject
  - 기존 구간보다 크거나 같은 거리의 구간 생성 요청은 reject

|| pseudo code
```agsl
Sections.java
// addSection
if ( sections 사이즈가 0 이면 ) {
    sections.add(section);
    return;
}
예외처리(상행역과 하행역 모두 이미 구간에 등록되어 있다면 reject)
예외처리(구간 request 로 들어온 상행역, 하행역이 해당 노선에 모두 존재하지 않으면 reject)
case ( 상행종점 구간 생성 요청 ) {
    addSectionAtFirst(Section);
}
case ( 하행종점 구간 생성 요청 ) {
    addSectionAtLast(Section);
}
case ( 사이구간 생성 요청 ) {
    addSectionBetween(Section);
}
```

### addSectionAtFirst
```agsl
private void addSectionAtFirst(...){
    if( 상행 종점역과, 새로 추가할 구간의 하행역이 다르면){
        예외발생!
    }
    sections.add(맨처음, 새 구간);
}
```

### addSectionAtLast
```agsl
private void addSectionAtLast(...){
    if( 하행 종점역과, 새로 추가할 구간의 상행역이 다르면){
        예외발생!
    }
    section.add(마지막, 새 구간);
}
```

### addSectionBetween
```agsl
private void addSectionBetween(...){
    for( i : sections 순회 ){
        station : section 내에 속한 상행역에 대하여 ) {
        if (새로 추가할 구간의 상행역이 station 과 같다면 ) {
             i 번째에 새로운 구간을 추가
             i+1 번째의 구간을 update
            }
        } 
    } 
}

case i)
예를 들어 [A,B] - [B,C] 에 [B,X] 를 추가하는 상황이라면
목표 상태는 [A,B] - [B,X] - [X,C] 이라고 설정하자.

[B, X] 의 B 와 일치하는 첫 번째 구간 [A, B] 의 B 이다. (i=1)
그러면 (i = 1) 에
   새로운 구간 [B, X] 를 추가한다.
        sections = [A,B] - [B,X] - [B,C]
그러면 (i+1=2) 구간인 [B,C] 의 상행역을 새로운 구간의 하행역인 X 으로 변경해주면
        sections = [A,B] - [B,X] - [X, C] 가 된다.
       

case ii) 
이번에는 [A,B] - [B,C] 에 [A,X] 를 추가하는 상황이라면
    목표 상태는 [A,X] - [X,B] - [B,C] 이다.
그러면 (i = 0) 에
    새로운 구간인 [A, X] 를 추가한다.
    sections = [A,X] - [A,B] - [B,C]
그리고 (i+1 = 1) 구간인 [A,B] 의 상행역을 새로운 구간의 하행역인 X 으로 변경하면
    sections = [A,X] - [X,B] - [B,C] 가 된다.
```
