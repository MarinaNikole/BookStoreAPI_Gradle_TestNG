package tests.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class ApiBase {
    final static String BASE_URI = "https://demoqa.com/";

    private final RequestSpecification spec;


    public ApiBase() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    public ApiBase(String token) {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
    public  Response getRequest(String endpoint, Integer responseCode) {
        Response response = given()
                .spec(spec)
                .when()
                .log().all()
                .get(endpoint)
                .then()
                .log().all()
                .statusCode(responseCode)
                .extract().response();
        return response;
    }

    public Response getRequestWithParam(String endPoint, int responseCode, String paramName, String value) {
        Response response = RestAssured.given()
                .spec(spec)
                .when()
                .pathParam(paramName, value)
                .log().all()
                .get(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public Response getRequestWithQuery(String endPoint, int responseCode, String queryName, String value) {
        Response response = RestAssured.given()
                .spec(spec)
                .queryParam(queryName, value)
                .when()
                .log().all()
                .get(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public  Response postRequest(String endpoint, Integer responseCode, Object body) {
        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .log().all()
                .post(endpoint)
                .then()
                .log().all()
                .statusCode(responseCode)
                .extract().response();
        return response;
    }

    public Response deleteRequest(String endpoint, Integer responseCode, Object body) {
        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .log().all()
                .delete(endpoint)
                .then()
                .log().all()
                .statusCode(responseCode)
                .extract().response();
        return response;
    }

        public Response deleteRequestWithQuery(String endpoint, Integer responseCode, String userId) {
            Response response = given()
                    .spec(spec)
                    .queryParam("UserId", userId)
                    .when()
                    .log().all()
                    .delete(endpoint)
                    .then()
                    .log().all()
                    .statusCode(responseCode)
                    .extract().response();
            return response;
    }

    public Response deleteRequestWithParam(String endpoint, Integer responseCode, String paramName, String value) {
        Response response = given()
                .spec(spec)
                .pathParam(paramName, value)
                .when()
                .log().all()
                .delete(endpoint)
                .then()
                .log().all()
                .statusCode(responseCode)
                .extract().response();
        return response;
    }

    public  Response putRequest(String endpoint, Integer responseCode, Object body, String paramName, String value) {
        Response response = given()
                .spec(spec)
                .pathParam(paramName, value)
                .body(body)
                .when()
                .log().all()
                .put(endpoint)
                .then()
                .log().all()
                .statusCode(responseCode)
                .extract().response();
        return response;
    }

}
