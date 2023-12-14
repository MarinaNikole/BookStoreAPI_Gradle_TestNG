package dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookListModal {
    private String userId;

    private ArrayList<Object> collectionOfIsbns;
}
