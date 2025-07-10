package ru.praktikum.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.scooter.clients.OrderClient;
import ru.praktikum.scooter.models.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderTest {
    private final OrderClient orderClient = new OrderClient();
    private final List<String> colors;

    public OrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Colors: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @DisplayName("Can create order with different colors")
    public void canCreateOrderWithDifferentColors() {
        Order order = new Order(
                "Test",
                "User",
                "Address 123",
                "4",
                "+79998887766",
                5,
                "2023-06-06",
                "Test comment",
                colors
        );

        Response response = orderClient.create(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .body("track", greaterThan(0));
    }

    @Test
    @DisplayName("Can get orders list")
    public void canGetOrdersList() {
        Response response = orderClient.getOrders();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0))
                .body("pageInfo", notNullValue());
    }
}
