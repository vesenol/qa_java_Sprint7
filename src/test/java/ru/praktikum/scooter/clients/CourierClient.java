package ru.praktikum.scooter.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.scooter.models.Courier;
import ru.praktikum.scooter.models.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Create courier")
    public Response create(Courier courier) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Login courier")
    public Response login(CourierCredentials credentials) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Delete courier by id {courierId}")
    public Response delete(int courierId) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}
