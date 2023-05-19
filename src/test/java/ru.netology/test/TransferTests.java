package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;
import ru.netology.data.MrDataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationCodePage;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class TransferTests {

    @Test
    @DisplayName("transfer from card 2 to card 1")
    public void shdAddFundsFromSecondCardToFirst() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(1);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard secondCardNmb = MrDataHelper.generateCardNmb2(defaultVasya);
        addFundsPage.topupBalanceForCard(secondCardNmb.getCardNumber(), 5928);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 + 5928;
        Assertions.assertEquals(balanceExpected1, balanceActual1);

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 - 5928;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
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
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard firstCardNmb = MrDataHelper.generateCardNmb1(defaultVasya);
        addFundsPage.topupBalanceForCard(firstCardNmb.getCardNumber(), 5928);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 5928;
        Assertions.assertEquals(balanceExpected1, balanceActual1);

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 5928;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }

    @Test
    @DisplayName("transfer from card 1 to card 2 over limit") // тот самый баг!
    public void shdAddFundsFromFirstCardToSecondIfInsufficientFunds() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard firstCardNmb = MrDataHelper.generateCardNmb1(defaultVasya);
        addFundsPage.topupBalanceForCard(firstCardNmb.getCardNumber(), 10001);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 0; // не знаю, чего тут ожидать - видимо, предполагается, что списания не произойдёт
        Assertions.assertEquals(balanceExpected1, balanceActual1);

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 0;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }

    @Test
    @DisplayName("transfer from card 1 to card 2 under limit value") // продолжаем проверять граничные значения
    public void shdAddFundsFromFirstCardToSecondIfUnderLimit() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard firstCardNmb = MrDataHelper.generateCardNmb1(defaultVasya);
        addFundsPage.topupBalanceForCard(firstCardNmb.getCardNumber(), 9999);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 9999;
        Assertions.assertEquals(balanceExpected1, balanceActual1);

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 9999;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }

    @Test
    @DisplayName("transfer from card 1 to card 2 with exact limit value") // продолжаем проверять граничные значения
    public void shdAddFundsFromFirstCardToSecondIfEqualToLimit() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard firstCardNmb = MrDataHelper.generateCardNmb1(defaultVasya);
        addFundsPage.topupBalanceForCard(firstCardNmb.getCardNumber(), 10000);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 10000;
        Assertions.assertEquals(balanceExpected1, balanceActual1);

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 10000;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }

    @Test
    @DisplayName("transfer from card 2 to same card") // в самом приложении нет такой проверки, однако ввести один номер для карт зачисления и списания оно позволяет
    public void shdAddFundsFromSecondCardToItself() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard secondCardNmb = MrDataHelper.generateCardNmb2(defaultVasya);
        addFundsPage.topupBalanceForCard(secondCardNmb.getCardNumber(), 300);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 0;
        Assertions.assertEquals(balanceExpected1, balanceActual1);  // к счастью, списания не происходит

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 0;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }

    @Test
    @DisplayName("transfer from card 2 to card 1 if negative sum") // в самом приложении нет такой проверки, однако ввести один номер для карт зачисления и списания оно позволяет
    public void shdAddFundsFromSecondCardToFirstIfBelowZero() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        LoginPage newLoginPage = new LoginPage();
        VerificationCodePage newCodePage = newLoginPage.enterLoginAndPassword();
        newCodePage.enterVerificationCode();
        DashboardPage cardsPage = new DashboardPage();
        int balanceBeforeTest1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceBeforeTest2 = cardsPage.retrieveBalanceForCardByPosition(2);
        TransferPage addFundsPage = cardsPage.proceedToTransferPage(2);
        MrDataHelper.NewUserEntry defaultVasya = MrDataHelper.generateDefaultUser();
        MrDataHelper.UserCard secondCardNmb = MrDataHelper.generateCardNmb2(defaultVasya);
        addFundsPage.topupBalanceForCard(secondCardNmb.getCardNumber(), -273);

        int balanceActual1 = cardsPage.retrieveBalanceForCardByPosition(1);
        int balanceExpected1 = balanceBeforeTest1 - 0;
        Assertions.assertEquals(balanceExpected1, balanceActual1);  // видимо, или такая проверка уже встроена, или поле не допускает ввода символа "-"

        int balanceActual2 = cardsPage.retrieveBalanceForCardByPosition(2);
        int balanceExpected2 = balanceBeforeTest2 + 0;
        Assertions.assertEquals(balanceExpected2, balanceActual2);
    }
}
