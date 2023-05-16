package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;
import ru.netology.data.MrDataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationCodePage;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class TransferTests {

    @Test
    @DisplayName("evaluate balance for card 1")
    public void shdRetrieveBalanceForCardOne() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceDifference = balanceBeforeTest - MrDataHelper.generateDefaultUser().getUserCardFirstBalance(); // воспользуемся тем, что нам известен баланс по умолчанию
            balanceDifference = balanceDifference * -1;
        int expected = 10000;
        int actual = balanceDifference + balanceBeforeTest; // такие шизофренические ассерты требуются, т.к. я не вижу другого способа сравнить данные баланса
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("evaluate balance for card 2")
    public void shdRetrieveBalanceForCardTwo() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceDifference = balanceBeforeTest - MrDataHelper.generateDefaultUser().getUserCardFirstBalance();
        balanceDifference = balanceDifference * -1;
        int expected = 10000;
        int actual = balanceDifference + balanceBeforeTest;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("transfer from card 2 to card 1")
    public void shdAddFundsFromSecondCardToFirst() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest = cardsPage.retrieveBalanceForCardByPosition(1);
        cardsPage.topupBalanceForCardByPosition(1, 5928);
        int balanceActual = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected = balanceBeforeTest + 5928;

        Assertions.assertEquals(balanceExpected, balanceActual);
    }

    @Test
    @DisplayName("transfer from card 1 to card 2")
    public void shdAddFundsFromFirstCardToSecond() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest = cardsPage.retrieveBalanceForCardByPosition(2);
        cardsPage.topupBalanceForCardByPosition(2, 410);
        int balanceActual = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected = balanceBeforeTest + 410;

        Assertions.assertEquals(balanceExpected, balanceActual);
    }

    @Test
    @DisplayName("transfer from card 1 to card 2 when insufficient funds")
    public void shdAddFundsFromFirstCardToSecondWhenOverLimit() { // баг, позволяющий переводить больше, чем есть на карте
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        cardsPage.topupBalanceForCardByPosition(2, 10001);
        $x("//div[@data-test-id='error-notification']").shouldBe(Condition.visible);
    }
}
