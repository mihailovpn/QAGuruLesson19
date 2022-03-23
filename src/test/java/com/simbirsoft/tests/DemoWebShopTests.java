package com.simbirsoft.tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.simbirsoft.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Tag("Regress")
@Story("Adding to cart")
public class DemoWebShopTests {
    protected Cookie cookieWeb;

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }


    @Test
    @DisplayName("Adding smartphone to the cart")
    void addingSmartphoneTest() {
                    given()
                            .filter(customLogFilter().withCustomTemplates())
                            .log().all()
                            .when()
                            .post("/addproducttocart/details/43/1")
                            .then()
                            .log().all()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("updatetopcartsectionhtml", is("(1)"));
    }
}
