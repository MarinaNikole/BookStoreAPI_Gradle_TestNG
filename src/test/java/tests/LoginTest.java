package tests;

import dto.LoginViewModel;

import tests.helpers.ApiBase;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class LoginTest extends ApiBase {

    @Test
    public void successfulLogin(){
            LoginViewModel requestBody = LoginViewModel.builder()
                .userName("Ronny")
                .password("123Ron321&").
                build();
        String userName = postRequest("/Account/v1/Login", 200, requestBody).jsonPath().getString("username");
        assertEquals("Ronny", userName, "Expected Username does NOT correspond to actual");
    }
}
