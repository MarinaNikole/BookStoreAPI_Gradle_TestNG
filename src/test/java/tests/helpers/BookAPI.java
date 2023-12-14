package tests.helpers;

import dto.BookListModal;
import dto.Books;
import dto.Isbns;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


public class BookAPI extends ApiBase {
    ApiBase apiBaseToken;

    public String getISBN(int bookIndex) {
        return getRequest("/BookStore/v1/Books", 200).body()
                .jsonPath().getList("books", Books.class)
                .get(bookIndex).getIsbn();
    }

    public void addOneBook(String isbn, String userID, String token) {
        apiBaseToken = new ApiBase(token);
        ArrayList<Object> collection = new ArrayList<>();
        collection.add(new Isbns(isbn));

        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(collection)
                .build();
        String isbnFromResponse = apiBaseToken.postRequest("/BookStore/v1/Books", 201, reqBody).getBody().jsonPath().getString("books[0].isbn");
        assertEquals(isbn, isbnFromResponse, "Isbn from request does NOT correspond to Isbn from response");
    }

    public void addListOfSixBooks(String userID, String token) {
        List<Books> bookList = getRequest("/BookStore/v1/Books", 200).body().jsonPath().getList("books", Books.class);
        ArrayList<Object> requestCollection = new ArrayList<>();
        requestCollection.add(new Isbns(bookList.get(0).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(1).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(2).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(3).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(4).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(5).getIsbn()));

        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(requestCollection)
                .build();
        apiBaseToken = new ApiBase(token);
        List<Isbns> responseCollection = apiBaseToken.postRequest("/BookStore/v1/Books", 201, reqBody)
                .getBody().
                jsonPath().
                getList("books", Isbns.class);
        assertEquals(requestCollection, responseCollection, "ExpectedCollection does NOT correspond to responseCollection");
    }
}