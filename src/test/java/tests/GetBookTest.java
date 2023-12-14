package tests;

import dto.Books;

import tests.helpers.ApiBase;
import tests.helpers.BookAPI;

import java.util.List;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class GetBookTest extends ApiBase {

    BookAPI bookAPI;
    String endpoint = "/BookStore/v1/Books";

    @Test
    public void getAllBooksTest() {
        List<Books> response = getRequest(endpoint, 200).body().jsonPath().getList("books", Books.class);
        assertEquals(8, response.size(), "Expected quantity of book collection does NOT correspond to from response");
        assertEquals("Git Pocket Guide", response.get(0).getTitle(),
                "Expected title of the book does NOT correspond to from response");
    }

    @Test
    public void getOneBook() {
        bookAPI = new BookAPI();
        String expectedIsbn = bookAPI.getISBN(1);

        Books bookFromResponse = getRequestWithQuery("/BookStore/v1/Book", 200, "ISBN", expectedIsbn)
                .body().jsonPath().getObject("", Books.class);
        String isbnFromResponse = bookFromResponse.getIsbn();
        assertEquals(expectedIsbn, isbnFromResponse, "expected ISBN is not equal to ISBN from response");
    }

    @Test
    public void getOneBookWithInvalidIsbn() {
        bookAPI = new BookAPI();
        String invalidIsbn = "1111111111";

        String errorMessage = getRequestWithQuery("/BookStore/v1/Book", 400, "ISBN", invalidIsbn)
                .body().jsonPath().getString("message");
        assertEquals("ISBN supplied is not available in Books Collection!", errorMessage,
                "Error message from request does NOT correspond to from response");
    }

}
