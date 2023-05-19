package ru.netology.page;

import com.codeborne.selenide.Condition;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class TransferPage {

    public DashboardPage topupBalanceForCard(String cardNumber, int sumToAdd) {
        String sum4input = Integer.toString(sumToAdd);

        $x("//div[@data-test-id='amount']/descendant::input[@class='input__control']").setValue(sum4input);
        $x("//span[@data-test-id='from']/descendant::input[@class='input__control']").setValue(cardNumber);
        // таким образом мы ввели номер другой карты из созданного выше массива
        $x("//button[@data-test-id='action-transfer']").click(); // отправляем клик по кнопке "Пополнить"
        sleep(5000);
        $x("//h2[@data-test-id='dashboard']").shouldBe(Condition.visible); // проверяем, случился ли переход на предыдущую страницу (в раздел "Ваши карты")
        return new DashboardPage();
    }
}
