package ru.praktikum.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.clients.CourierClient;
import ru.praktikum.scooter.models.Courier;
import ru.praktikum.scooter.models.CourierCredentials;

import static org.hamcrest.Matchers.*;

public class LoginTest {
    private final CourierClient courierClient = new CourierClient();
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 10000));

        courier = new Courier(
                "testCourier" + System.currentTimeMillis(),
                "password",
                "Test Name"
        );
        courierClient.create(courier)
                .then()
                .statusCode(201);

        courierId = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()))
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    @DisplayName("Courier can login with valid credentials")
    public void courierCanLogin() {
        Response response = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));
    }

    @Test
    @DisplayName("Login requires all fields (empty password)")
    public void loginRequiresAllFields() {
        Response response = courierClient.login(new CourierCredentials(courier.getLogin(), ""));
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login with wrong password returns error")
    public void loginWithWrongCredentialsReturnsError() {
        Response response = courierClient.login(new CourierCredentials(courier.getLogin(), "wrongpassword"));
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login non-existent user returns error")
    public void loginNonExistentUserReturnsError() {
        Response response = courierClient.login(
                new CourierCredentials("nonexistent_" + System.currentTimeMillis(), "anypassword"));
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
