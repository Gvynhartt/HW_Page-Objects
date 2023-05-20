package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import ru.netology.data.MrDataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LoginPage {
    private String loginXpath = "//span[@data-test-id='login']/descendant::input[@name='login']";
    private String passwordXpath = "//span[@data-test-id='password']/descendant::input[@name='password']";
    private String continueXpath = "//button[@data-test-id='action-login']/span[@class='button__content']";

    public VerificationCodePage enterLoginAndPassword() { // здесь мы генерируем дефолтные данные, т.к. тестировать именно авторизацию не нужно
        $x(loginXpath).setValue(MrDataHelper.generateDefaultUser().getUserLogin());
        $x(passwordXpath).setValue(MrDataHelper.generateDefaultUser().getUserPassword());
        $x(continueXpath).click();
        $x("//span[@data-test-id='code']").shouldBe(Condition.visible, Duration.ofMillis(5000));
        // здесь ожидание перехода на URL заменена проверкой видимости элемента
        return new VerificationCodePage();
    }
}
