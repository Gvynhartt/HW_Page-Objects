package ru.netology.page;

import com.codeborne.selenide.Condition;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class TransferPage {

    public DashboardPage topupBalanceForCard(String cardNumber, int sumToAdd) {
        String sum4input = Integer.toString(sumToAdd);
        $x("//button[@data-test-id='action-transfer']").shouldBe(Condition.visible);
        $x("//div[@data-test-id='amount']/descendant::input[@class='input__control']").setValue(sum4input);
        $x("//span[@data-test-id='from']/descendant::input[@class='input__control']").setValue(cardNumber);
        // таким образом мы ввели номер другой карты из созданного выше массива
        $x("//button[@data-test-id='action-transfer']").click(); // отправляем клик по кнопке "Пополнить"
        return new DashboardPage();
    }
}
