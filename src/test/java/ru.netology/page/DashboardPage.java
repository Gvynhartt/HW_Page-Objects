package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class DashboardPage {

    private ElementsCollection cardsCollection = $$x("//li[@class='list__item']/div[@data-test-id]");
    private ElementsCollection buttonsCollection = $$x("//button[@data-test-id='action-deposit']");

    private int cardBalance; // сохран€ет баланс на рассматриваемой карте

    private String cardText; // сохран€ет отображаемый на странице текст найденного элемента, из которого далее будем получать баланс

    private final String inSubstr = "баланс: ";
    private final String outSubstr = " р.";

    private String[] userCards = new String[2]; // заводим массив с номерами карт дл€ переводов

    public int retrieveBalanceForCardByPosition(int cardPosition) { // в тестах мы будем обращатьс€ к картам по позиции на странице (перва€ - втора€, 1 - 2)
        if (cardPosition == 1) {
            cardText = cardsCollection.first().getText(); // получаем текст нужного элемента
        } else {
            cardText = cardsCollection.last().getText();
        }

        int inPoint = cardText.indexOf(inSubstr);
        int outPoint = cardText.indexOf(outSubstr);
        String sumSubstr = cardText.substring(inPoint + inSubstr.length(), outPoint);
        cardBalance = Integer.parseInt(sumSubstr);
        return cardBalance;
    }

    public void topupBalanceForCardByPosition(int cardPosition, int sumToAdd) {
        cardPosition = cardPosition - 1; // подготавливаем позицию карты дл€ дальнейших манипул€ций
        String sum4input = Integer.toString(sumToAdd);
        int otherCardBalance = retrieveBalanceForCardByPosition((cardPosition-1)*-1); // сумма нужна дл€ проверки условий начала операции
        // така€ конструкци€ позвол€ет нам извлечь баланс дл€ второй карты, получив еЄ индекс от первой (1 -> 0, 0 -> 1)

        userCards[0] = MrDataHelper.generateDefaultUser().getUserCardFirstNumber();
        userCards[1] = MrDataHelper.generateDefaultUser().getUserCardSecondNumber();

        if (otherCardBalance < sumToAdd) { // вшиваем проверку, чтоб сумма пополнени€ не превышала баланса второй карты
            sumToAdd = otherCardBalance;
        }

        buttonsCollection.get(cardPosition).click(); // отправл€ем клик по кнопке соответствующей карты
        $x("//button[@data-test-id='action-transfer']").should(Condition.visible);
        $x("//div[@data-test-id='amount']/descendant::input[@class='input__control']").setValue(sum4input);
        $x("//span[@data-test-id='from']/descendant::input[@class='input__control']").setValue(userCards[(cardPosition-1)*-1]);
        // таким образом мы ввели номер другой карты из созданного выше массива
        $x("//button[@data-test-id='action-transfer']").click(); // отправл€ем клик по кнопке "ѕополнить"
        $x("//h2[@data-test-id='dashboard']").shouldBe(Condition.visible); // провер€ем, случилс€ ли переход на предыдущую страницу (в раздел "¬аши карты")
    }
}
