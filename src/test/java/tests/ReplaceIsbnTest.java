package tests;

import com.github.javafaker.Faker;
import dto.ReplaceIsbn;

import org.testng.annotations.Test;
import tests.helpers.ApiBase;
import tests.helpers.BookAPI;
import tests.helpers.DeleteAPI;
import tests.helpers.UserAPI;


public class ReplaceIsbnTest extends ApiBase {
    ApiBase apiBaseToken;
    UserAPI userApi;
    DeleteAPI deleteApi;
    BookAPI bookApi;

    Faker faker = new Faker();
    String userName = faker.name().username();
    String password = "123Ron321&";

   @Test
    public void replaceIsbn() {
        userApi = new UserAPI();
        String userId = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        bookApi = new BookAPI();
        bookApi.addListOfSixBooks(userId, token);

        String isbn1 = bookApi.getISBN(0);
        String isbn2 = bookApi.getISBN(1);


        ReplaceIsbn bodyRequest = ReplaceIsbn.builder()
                .userId(userId)
                .isbn(isbn1)
                .build();
        apiBaseToken = new ApiBase(token);
        apiBaseToken.putRequest("/BookStore/v1/Books/{ISBN}", 200, bodyRequest, "ISBN", isbn2);


        deleteApi = new DeleteAPI();
        deleteApi.deleteAllBooks(userId, token);
    }

}