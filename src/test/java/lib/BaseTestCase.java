package lib;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    protected Response getResponseWithoutParams(String url) {
        Response response = RestAssured
                .get(url)
                .andReturn();
        return response;
    }

    protected JsonPath getJsonPathWithoutParameters(String url) {
        JsonPath response = RestAssured
                .get(url)
                .jsonPath();
        return response;
    }

    protected JsonPath getJsonPathWithUserAgent(String url, String name) {
        Map<String, String> headerUserAgent = new HashMap<>();
        headerUserAgent.put("User-Agent", name);

        JsonPath response = RestAssured
                .given()
                .headers(headerUserAgent)
                .when()
                .get(url)
                .jsonPath();
        return response;
    }


    protected String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name), name + ": This header isn't found");
        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();
        assertTrue(cookies.containsKey(name), name + ": This cookie isn't found");
        return cookies.get(name);
    }

    protected String getJsonValue(JsonPath response, String key) {
        String value = response.get(key);
        assertFalse(value.equals(null));
        return value;
    }

    protected int getIntFromJson(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getInt(name);
    }

    protected String getSingleHeaderFromJson(Response response, String name, String pole) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getString(name + pole);
    }

    protected List getSingleValueFromJson(Response response, String name, String pole) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().get(name + pole);
    }

}