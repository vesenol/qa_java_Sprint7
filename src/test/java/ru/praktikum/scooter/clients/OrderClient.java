package ru.praktikum.scooter.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.scooter.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Create order")
    public Response create(Order order) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Get orders list")
    public Response getOrders() {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_PATH);
    }
}
