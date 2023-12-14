package dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOfBooks {
    public String isbn;
}
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
 class Root{
    public ArrayList<ListOfBooks> books;
}
