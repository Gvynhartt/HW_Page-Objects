package ru.netology.page;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class LoginPage {
    private String loginXpath = "//span[@data-test-id='login']/descendant::input[@name='login']";
    private String passwordXpath = "//span[@data-test-id='password']/descendant::input[@name='password']";
    private String continueXpath = "//button[@data-test-id='action-login']/span[@class='button__content']";

    public VerificationCodePage enterLoginAndPassword() { // здесь мы генерируем дефолтные данные, т.к. тестировать именно авторизацию не нужно
        $x(loginXpath).setValue(MrDataHelper.generateDefaultUser().getUserLogin());
        $x(passwordXpath).setValue(MrDataHelper.generateDefaultUser().getUserPassword());
        $x(continueXpath).click();
        sleep(5000);
        String actualUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        String expectedUrl = "http://localhost:9999/verification";
        Assertions.assertEquals(expectedUrl, actualUrl);
        return new VerificationCodePage();
    }
}
