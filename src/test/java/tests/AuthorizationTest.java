package tests;

import com.github.javafaker.Faker;
import dto.LoginViewModel;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import tests.helpers.ApiBase;
import tests.helpers.UserAPI;



public class AuthorizationTest extends ApiBase {
    UserAPI userApi;
    Faker faker = new Faker();

    String password = "123Ron321&";
    String endPont = "/Account/v1/Authorized";

    @Test
    public void successfulAuthorization() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        userApi.generateToken(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(userName)
                .password(password).
                build();
        String response = postRequest(endPont, 200, requestBody).asString();
        assertEquals("true", response);
    }

    @Test
    public void unsuccessfulAuthorization() {
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(userName)
                .password(password).
                build();
        String response = postRequest(endPont, 200, requestBody).asString();
        assertEquals("false", response);
    }

    @Test
    public void authorizationWithoutCreatedUser(){
        String userName = faker.name().username();
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(userName)
                .password(password).
                build();
        String responseMessage = postRequest(endPont, 404, requestBody).jsonPath().getString("message");
        assertEquals("User not found!", responseMessage, "Expected message does NOT correspond to from response");
    }

    @Test
    public void invalidPasswordAuthorization(){
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(userName)
                .password("987Aqua078#").
                build();
        String responseMessage = postRequest(endPont, 404, requestBody).jsonPath().getString("message");
        assertEquals("User not found!", responseMessage, "Expected message does NOT correspond to from response");
    }

    @Test
    public void invalidUserNameAuthorization(){
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName("Aqua")
                .password(password).
                build();
        String responseMessage = postRequest(endPont, 404, requestBody).jsonPath().getString("message");
        assertEquals("User not found!", responseMessage, "Expected message does NOT correspond to from response");
    }

    @Test
    public void emptyPasswordAuthorization(){
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName(userName)
                .password("").
                build();
        String responseMessage = postRequest(endPont, 400, requestBody).jsonPath().getString("message");
        assertEquals("UserName and Password required.", responseMessage,
                "Expected message does NOT correspond to from response");
    }

    @Test
    public void emptyUserNameAuthorization(){
        String userName = faker.name().username();
        userApi = new UserAPI();
        userApi.createUser(userName, password);
        LoginViewModel requestBody = LoginViewModel.builder()
                .userName("")
                .password(password).
                build();
        String responseMessage = postRequest(endPont, 400, requestBody).jsonPath().getString("message");
        assertEquals("UserName and Password required.", responseMessage,
                "Expected message does NOT correspond to from response");
    }

}
