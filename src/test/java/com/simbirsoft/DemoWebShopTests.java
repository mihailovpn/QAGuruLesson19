package com.simbirsoft;

import com.codeborne.selenide.Configuration;
import com.simbirsoft.helpers.AllureRestAssuredFilter;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import io.qameta.allure.restassured.AllureRestAssured;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


@Story("Login tests")
public class DemoWebShopTests {
    public String userLogin = "qaguru@qa.guru";
    public String userPassword = "qaguru@qa.guru1";

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithCookieTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .filter(AllureRestAssuredFilter.withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", userLogin)
                            .formParam("Password", userPassword)
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
            step("Open smartphone page", () ->
                    open("/smartphone"));

            step("Add smartphone to cart", () ->
                    $(".add-to-cart-button").click());

            step("", () ->
                    given()
                            .cookie(authorizationCookie)
                            .when()
                            .get("/cart")
                            .then()
                            .statusCode(200));
        });


    }
}
