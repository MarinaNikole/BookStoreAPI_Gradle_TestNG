package tests;

import com.github.javafaker.Faker;
import dto.LoginViewModel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import tests.helpers.ApiBase;
import tests.helpers.DeleteAPI;
import tests.helpers.UserAPI;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class CreateUserTest extends ApiBase {
    DeleteAPI deleteAPI = new DeleteAPI();
    UserAPI userAPI = new UserAPI();
    Faker faker = new Faker();
    String validPassword = "123Ron321&";

    @Test
    public void successfulCreateUser() {
        String validUserName = faker.name().username();
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(validUserName)
                .password(validPassword).
                build();
        JsonPath responseBody= postRequest("/Account/v1/User", 201, requestBody).jsonPath();
        String responseUserName = responseBody.getString("username");
        assertEquals(validUserName, responseUserName, "UserName from request does NOT correspond to UserName from response");

        String userId = responseBody.getString("userID");
        String token = userAPI.generateToken(validUserName, validPassword);
        deleteAPI.deleteAccount(userId, token);
    }

    @Test
    public void createUserWithInvalidEndPoint() {
        String validUserName = faker.name().username();
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(validUserName)
                .password(validPassword).
                build();
        postRequest("/Account/v1/User/3457", 404, requestBody).jsonPath();
    }


    @Test
    public void createAnExistingUser() {
        String validUserName = faker.name().username();
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(validUserName)
                .password(validPassword).build();
        Response responseFirst= postRequest("/Account/v1/User", 201, requestBody);


        LoginViewModel reqBody = LoginViewModel.builder()
                .userName(validUserName)
                .password(validPassword).build();
        String errorMessage = postRequest("/Account/v1/User", 406, reqBody).jsonPath().getString("message");
        assertEquals("User exists!", errorMessage, "Expected error message does NOT correspond to from response");


        String userId = responseFirst.jsonPath().getString("userID");
        String token = userAPI.generateToken(validUserName, validPassword);
        deleteAPI.deleteAccount(userId, token);
    }

    @Test
    public void invalidPasswordCreateUser() {
        String validUserName = faker.name().username();
        String expectedErrorMessage = "Passwords must have at least one non alphanumeric character, " +
                "one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), " +
                "one special character and Password must be eight characters or longer.";

        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(validUserName)
                .password("12345678").
                build();
        String actualErrorMessage = postRequest("/Account/v1/User", 400, requestBody)
                .jsonPath().getString("message");
        assertEquals(expectedErrorMessage, actualErrorMessage,
                "Expected error message does NOT correspond to Actual Error message");
    }

    @Test
    public void emptyBodyCreateUser(){
        String expectedErrorMessage = "UserName and Password required.";

        LoginViewModel requestBody = LoginViewModel.builder()
                .userName("")
                .password("").
                build();
        String actualErrorMessage = postRequest("/Account/v1/User", 400, requestBody).jsonPath()
                .getString("message");
        assertEquals(expectedErrorMessage, actualErrorMessage,
                "Expected error message does NOT correspond to Actual Error message");
    }

    @Test
    public void emptyUserNameCreateUser(){
        String expectedErrorMessage = "UserName and Password required.";

        LoginViewModel requestBody = LoginViewModel.builder()
                .userName("")
                .password(validPassword).
                build();
        String actualErrorMessage = postRequest("/Account/v1/User", 400, requestBody)
                .jsonPath().getString("message");
        assertEquals(expectedErrorMessage, actualErrorMessage,
                "Expected error message does NOT correspond to Actual Error message");
    }

    @Test
    public void emptyPasswordCreateUser(){
        String expectedErrorMessage = "UserName and Password required.";

        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(validPassword)
                .password("").
                build();
        String actualErrorMessage = postRequest("/Account/v1/User", 400, requestBody)
                .jsonPath().getString("message");
        assertEquals(expectedErrorMessage, actualErrorMessage,
                "Expected error message does NOT correspond to Actual Error message");
    }
}
