package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class DashboardPage {

    private ElementsCollection cardsCollection = $$x("//li[@class='list__item']/div[@data-test-id]");
    private ElementsCollection buttonsCollection = $$x("//button[@data-test-id='action-deposit']");

    private int cardBalance; // сохраняет баланс на рассматриваемой карте

    private String cardText; // сохраняет отображаемый на странице текст найденного элемента, из которого далее будем получать баланс

    private final String inSubstr = "баланс: ";
    private final String outSubstr = " р.";

    private String[] userCards = new String[2]; // заводим массив с номерами карт для переводов

    public int retrieveBalanceForCardByPosition(int cardPosition) { // в тестах мы будем обращаться к картам по позиции на странице (первая - вторая, 1 - 2)
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
        cardPosition = cardPosition - 1; // подготавливаем позицию карты для дальнейших манипуляций
        String sum4input = Integer.toString(sumToAdd);
        int otherCardBalance = retrieveBalanceForCardByPosition((cardPosition-1)*-1); // сумма нужна для проверки условий начала операции
        // такая конструкция позволяет нам извлечь баланс для второй карты, получив её индекс от первой (1 -> 0, 0 -> 1)

        userCards[0] = MrDataHelper.generateDefaultUser().getUserCardFirstNumber();
        userCards[1] = MrDataHelper.generateDefaultUser().getUserCardSecondNumber();

        if (otherCardBalance < sumToAdd) { // вшиваем проверку, чтоб сумма пополнения не превышала баланса второй карты
            sumToAdd = otherCardBalance;
        }

        buttonsCollection.get(cardPosition).click(); // отправляем клик по кнопке соответствующей карты
        $x("//button[@data-test-id='action-transfer']").should(Condition.visible);
        $x("//div[@data-test-id='amount']/descendant::input[@class='input__control']").setValue(sum4input);
        $x("//span[@data-test-id='from']/descendant::input[@class='input__control']").setValue(userCards[(cardPosition-1)*-1]);
        // таким образом мы ввели номер другой карты из созданного выше массива
        $x("//button[@data-test-id='action-transfer']").click(); // отправляем клик по кнопке "Пополнить"
        $x("//h2[@data-test-id='dashboard']").shouldBe(Condition.visible); // проверяем, случился ли переход на предыдущую страницу (в раздел "Ваши карты")
    }
}
