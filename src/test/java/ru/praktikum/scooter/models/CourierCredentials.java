package ru.praktikum.scooter.models;

public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
