package dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplaceIsbn {
    private String userId;
    private String isbn;

}
