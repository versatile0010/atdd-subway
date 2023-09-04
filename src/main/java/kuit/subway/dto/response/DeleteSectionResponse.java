package kuit.subway.dto.response;

import kuit.subway.domain.Section;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class DeleteSectionResponse {
    private Long id;

    public static DeleteSectionResponse from(Section section) {
        return DeleteSectionResponse.builder()
                .id(section.getId())
                .build();
    }
}
