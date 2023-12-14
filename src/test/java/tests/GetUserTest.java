package tests;

import com.github.javafaker.Faker;

import tests.helpers.ApiBase;
import tests.helpers.UserAPI;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


public class GetUserTest extends ApiBase {
    ApiBase apiBaseToken;
    UserAPI userApi;

    Faker faker = new Faker();
    String password = "123Ron321&";
    String endpoint = "/Account/v1/User/{UserId}";
    @Test
    public void GetUser() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        String userId = userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        apiBaseToken = new ApiBase(token);
        String usernameResponse = apiBaseToken
                .getRequestWithParam(endpoint, 200, "UserId", userId)
                .body().jsonPath().getString("username");
        assertEquals(userName, usernameResponse, "Expected UserName does NOT correspond to UserNameFromResponse");
    }

    @Test
    public void GetUserWithInvalidUserId() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.getUserIdCreateUser(userName, password);
        String token = userApi.generateToken(userName, password);

        apiBaseToken = new ApiBase(token);
        String responseMessage= apiBaseToken
                .getRequestWithParam(endpoint, 404, "UserId", "5642987")
                .body().jsonPath().getString("message");
        assertEquals("User not found!", responseMessage, "Expected message does NOT correspond to response message");
    }

    @Test
    public void GetUserWithoutToken() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.getUserIdCreateUser(userName, password);

        String responseMessage= getRequestWithParam(endpoint, 401, "UserId", "5642987")
                .body().jsonPath().getString("message");
        assertEquals("User not authorized!", responseMessage, "Expected message does NOT correspond to response message");
    }
}