package ru.praktikum.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.clients.CourierClient;
import ru.praktikum.scooter.models.Courier;
import ru.praktikum.scooter.models.CourierCredentials;

import static org.hamcrest.Matchers.*;

public class CourierTest {
    private final CourierClient courierClient = new CourierClient();
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        // Генерация уникального логина для каждого теста
        courier = new Courier(
                "testCourier" + System.currentTimeMillis(),
                "password",
                "Test Name"
        );
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void courierCanBeCreated() {
        Response response = courierClient.create(courier);
        response.then()
                .statusCode(201)
                .body("ok", is(true));

        // Получаем ID для удаления
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.login(credentials)
                .then()
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCouriers() {
        // Сначала создаем курьера
        courierClient.create(courier);

        // Получаем ID для удаления
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.login(credentials)
                .then()
                .extract()
                .path("id");

        // Пытаемся создать такого же курьера еще раз
        Response response = courierClient.create(courier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Для создания курьера нужны все обязательные поля")
    public void createCourierRequiresAllFields() {
        Courier invalidCourier = new Courier(null, "password", "Test Name");

        Response response = courierClient.create(invalidCourier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
