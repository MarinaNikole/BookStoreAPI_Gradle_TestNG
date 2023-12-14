package tests;

import com.github.javafaker.Faker;
import dto.BookListModal;
import dto.Books;
import dto.Isbns;
import io.restassured.response.Response;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import tests.helpers.ApiBase;
import tests.helpers.BookAPI;
import tests.helpers.DeleteAPI;
import tests.helpers.UserAPI;

import java.util.ArrayList;
import java.util.List;



public class AddListOfBooksTest extends ApiBase {
    ApiBase apiBaseToken;
    UserAPI userApi;
    DeleteAPI deleteAPI;
    BookAPI bookAPI;
    Faker faker = new Faker();
    String password = "123Ron321&";
    String endpoint = "/BookStore/v1/Books";

   @Test
    public void addOneBook() {
       String userName = faker.name().username();
        userApi = new UserAPI();
        String userID = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        bookAPI = new BookAPI();
        String isbn = bookAPI.getISBN(6);

        deleteAPI = new DeleteAPI();
        deleteAPI.deleteAllBooks(userID, token);

// add One Book
        apiBaseToken = new ApiBase(token);
        ArrayList<Object> collection = new ArrayList<>();
        collection.add(new Isbns(isbn));

        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(collection)
                .build();
        Response response = apiBaseToken.postRequest("/BookStore/v1/Books", 201, reqBody);
        String isbnFromResponse = response.jsonPath().getString("books[0].isbn");
        assertEquals(isbn, isbnFromResponse, "Isbn from request does NOT correspond to Isbn from response");

        deleteAPI.deleteOneBook(isbn, userID, token);
    }

    @Test
    public void successfulAddListOfBooks() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        String userID = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        deleteAPI = new DeleteAPI();
        deleteAPI.deleteAllBooks(userID, token);

        List<Books> bookList = getRequest(endpoint, 200).body().jsonPath().getList("books", Books.class);
        ArrayList<Object> requestCollection = new ArrayList<>();
        requestCollection.add(new Isbns(bookList.get(0).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(1).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(7).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(4).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(5).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(2).getIsbn()));

        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(requestCollection)
                .build();
        apiBaseToken = new ApiBase(token);
        List<Isbns> responseCollection = apiBaseToken.postRequest(endpoint, 201, reqBody)
                .getBody().
                jsonPath().
                getList("books", Isbns.class);
        assertEquals(requestCollection, responseCollection,
                "ExpectedCollection does NOT correspond to responseCollection");

        deleteAPI.deleteAllBooks(userID, token);
    }

    @Test
    public void AddEmptyListOfBooks() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        String userID = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        deleteAPI = new DeleteAPI();
        deleteAPI.deleteAllBooks(userID, token);

        ArrayList<Object> requestCollection = new ArrayList<>();

        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(requestCollection)
                .build();
        apiBaseToken = new ApiBase(token);
        String responseErrorMessage = apiBaseToken.postRequest(endpoint, 400, reqBody)
                .getBody().jsonPath().getString("message");
        assertEquals("Collection of books required.", responseErrorMessage,
                "Expected error message does NOT correspond to from response");

        deleteAPI.deleteAllBooks(userID, token);
    }


    @Test
    public void AddListOfBooksWithInvalidUserId() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        String userID = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);
        deleteAPI = new DeleteAPI();
        deleteAPI.deleteAllBooks(userID, token);
        List<Books> bookList = getRequest(endpoint, 200).body().jsonPath().getList("books", Books.class);
        ArrayList<Object> requestCollection = new ArrayList<>();
        requestCollection.add(new Isbns(bookList.get(5).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(1).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(7).getIsbn()));

        BookListModal reqBody = BookListModal.builder()
                .userId("56739013")
                .collectionOfIsbns(requestCollection)
                .build();
        apiBaseToken = new ApiBase(token);
        String responseErrorMessage = apiBaseToken.postRequest(endpoint, 400, reqBody).getBody().
                jsonPath().getString("message");
        assertEquals("User Id not correct!", responseErrorMessage,
                "Expected error message does NOT correspond to from response");

        deleteAPI.deleteAllBooks(userID, token);
    }

    @Test
    public void AddListOfBooksWithoutToken() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        String userID = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);
        deleteAPI = new DeleteAPI();
        deleteAPI.deleteAllBooks(userID, token);
        List<Books> bookList = getRequest(endpoint, 200).body().jsonPath().getList("books", Books.class);
        ArrayList<Object> requestCollection = new ArrayList<>();
        requestCollection.add(new Isbns(bookList.get(5).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(1).getIsbn()));
        requestCollection.add(new Isbns(bookList.get(7).getIsbn()));


        BookListModal reqBody = BookListModal.builder()
                .userId(userID)
                .collectionOfIsbns(requestCollection)
                .build();
        String responseErrorMessage = postRequest(endpoint, 401, reqBody).getBody().
                jsonPath().getString("message");
        assertEquals("User not authorized!", responseErrorMessage,
                "Expected error message does NOT correspond to from response");

        deleteAPI.deleteAllBooks(userID, token);
    }
}
