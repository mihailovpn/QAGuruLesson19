package com.simbirsoft;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


@Story("Adding to cart")
public class DemoWebShopTests {
    protected Cookie cookieWeb;

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }


    @Test
    @DisplayName("Successful adding to cart (API + UI)")
    void loginWithCookieTest() {
            step("Open smartphone page", () ->
                    open("/smartphone"));

            step("Get cookie from browser", () ->
                    cookieWeb = getWebDriver().manage().getCookieNamed("Nop.customer"));

            step("Add smartphone to cart", () ->
                    $(".add-to-cart-button").click());

            step("Check amount of smartphones in cart", () ->
                    given()
                            .cookie(cookieWeb.toString())
                            .when()
                            .post("/addproducttocart/details/43/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("updatetopcartsectionhtml", is("(2)")) //2 - because 1 added by UI and 1 by API
            );


    }
}
