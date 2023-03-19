package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookieOnly(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithTokenOnly(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request without token and auth cookie")
    public Response makeGetRequestWithoutTokenAndCookie(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a GET-request with no body")
    public Response makeGetRequestWithNoBody(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request with no body")
    public Response makePostRequestNoBody(String url) {
        return given()
                .filter(new AllureRestAssured())
                .post(url)
                .andReturn();
    }


    @Step("Make a PUT-request")
    public Response makePutRequest(String urlPlusUserId, Map<String, String> editData, String header, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(urlPlusUserId)
                .andReturn();
    }

    @Step("Make a PUT-request without header & cookie")
    public Response makePutRequestWithoutHeaderAndCookie(String urlPlusUserId, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(urlPlusUserId)
                .andReturn();
    }

    @Step("Make a DELETE-request")
    public Response makeDeleteRequest(String urlPlusId, String header, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .delete(urlPlusId)
                .andReturn();
    }

    @Step("Make a ADDING-request")
    public Response makeAddingRequest(String urlPlusId, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .post(urlPlusId)
                .andReturn();
    }
}