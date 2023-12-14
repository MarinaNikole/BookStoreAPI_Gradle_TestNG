package dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter

public class GetUser {
    private String userId;
    private String userName;
    private ArrayList<Book> books;
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Getter
    @Setter
    public class Book{
        private String isbn;
        private String title;
        private String subTitle;
        private String author;
        private String publish_date;
        private String publisher;
        private int pages;
        private String description;
        private String website;

}
}
