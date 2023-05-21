package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private static ElementsCollection cardsCollection = $$x("//li[@class='list__item']/div[@data-test-id]");
    private ElementsCollection buttonsCollection = $$x("//button[@data-test-id='action-deposit']");

    private static int cardBalance; // сохраняет баланс на рассматриваемой карте

    private static String cardText; // сохраняет отображаемый на странице текст найденного элемента, из которого далее будем получать баланс

    private static final String inSubstr = "баланс: ";
    private static final String outSubstr = " р.";



    public static int retrieveBalanceForCardByPosition(int cardPosition) { // в тестах мы будем обращаться к картам по позиции на странице (первая - вторая, 1 - 2)
        cardPosition = cardPosition - 1;

        cardText = cardsCollection.get(cardPosition).getText();

        int inPoint = cardText.indexOf(inSubstr);
        int outPoint = cardText.indexOf(outSubstr);
        String sumSubstr = cardText.substring(inPoint + inSubstr.length(), outPoint);
        cardBalance = Integer.parseInt(sumSubstr);
        return cardBalance;
    }

    public TransferPage proceedToTransferPage(int cardPosition) {
        cardPosition = cardPosition - 1;
        buttonsCollection.get(cardPosition).click(); // т. к. коллекция из кнопок карт уже создана, не пропадать же добру
        return new TransferPage();
    }

}
