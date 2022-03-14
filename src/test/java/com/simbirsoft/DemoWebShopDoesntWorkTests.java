package com.simbirsoft;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


@Story("Ignore")
public class DemoWebShopDoesntWorkTests {
    protected Cookie cookieWeb;
    protected String amountSmartphonesInCart;
    protected String smartphoneInCartSelector = "input[name=\"itemquantity2294669\"]";
    public String userLogin = "qaguru@qa.guru";
    public String userPassword = "qaguru@qa.guru1";

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }
    @BeforeEach
    void prepareBrowser() {
        webdriver().driver().clearCookies();
    }


    @Test
    @DisplayName("Successful adding smartphone authorazed (doesn't work)")
    void loginWithCookieTest() {
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", userLogin)
                        .formParam("Password", userPassword)
                        .when()
                        .post("/login")
                        .then()
                        .extract()
                        .cookie("Nop.customer");

        step("Add smartphone to cart", () ->
                given()
                        .cookie("Nop.customer",authorizationCookie)
                        .when()
                        .post("/addproducttocart/details/43/1")
        );

        step("Check amount of smartphones in the cart", () -> {
            open("/cart");
            amountSmartphonesInCart = Selenide.$(smartphoneInCartSelector).getOwnText();
        });

        step("Add smartphone to cart", () ->
                    given()
                            .cookie("Nop.customer", authorizationCookie)
                            .when()
                            .post("/addproducttocart/details/43/1")
            );

        step("Check amount of smartphones in the cart after adding by API", () -> {
            open("/cart");
            Selenide.$(smartphoneInCartSelector).shouldHave(Condition.text((amountSmartphonesInCart + 1).toString()));
        });


    }
}
