package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationCodePage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTests {

    @Test
    @DisplayName("Login with default data")
    public void shdLoginWithHardcodeUser() { // проверяем логин с прописанным "пользователем"
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
    }
}
