package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class DashboardPage {

    private ElementsCollection cardsCollection = $$x("//li[@class='list__item']/div[@data-test-id]");
    private ElementsCollection buttonsCollection = $$x("//button[@data-test-id='action-deposit']");

    private int cardBalance; // ��������� ������ �� ��������������� �����

    private String cardText; // ��������� ������������ �� �������� ����� ���������� ��������, �� �������� ����� ����� �������� ������

    private final String inSubstr = "������: ";
    private final String outSubstr = " �.";

    private String[] userCards = new String[2]; // ������� ������ � �������� ���� ��� ���������

    public int retrieveBalanceForCardByPosition(int cardPosition) { // � ������ �� ����� ���������� � ������ �� ������� �� �������� (������ - ������, 1 - 2)
        if (cardPosition == 1) {
            cardText = cardsCollection.first().getText(); // �������� ����� ������� ��������
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
        cardPosition = cardPosition - 1; // �������������� ������� ����� ��� ���������� �����������
        String sum4input = Integer.toString(sumToAdd);
        int otherCardBalance = retrieveBalanceForCardByPosition((cardPosition-1)*-1); // ����� ����� ��� �������� ������� ������ ��������
        // ����� ����������� ��������� ��� ������� ������ ��� ������ �����, ������� � ������ �� ������ (1 -> 0, 0 -> 1)

        userCards[0] = MrDataHelper.generateDefaultUser().getUserCardFirstNumber();
        userCards[1] = MrDataHelper.generateDefaultUser().getUserCardSecondNumber();

        if (otherCardBalance < sumToAdd) { // ������� ��������, ���� ����� ���������� �� ��������� ������� ������ �����
            sumToAdd = otherCardBalance;
        }

        buttonsCollection.get(cardPosition).click(); // ���������� ���� �� ������ ��������������� �����
        $x("//button[@data-test-id='action-transfer']").should(Condition.visible);
        $x("//div[@data-test-id='amount']/descendant::input[@class='input__control']").setValue(sum4input);
        $x("//span[@data-test-id='from']/descendant::input[@class='input__control']").setValue(userCards[(cardPosition-1)*-1]);
        // ����� ������� �� ����� ����� ������ ����� �� ���������� ���� �������
        $x("//button[@data-test-id='action-transfer']").click(); // ���������� ���� �� ������ "���������"
        $x("//h2[@data-test-id='dashboard']").shouldBe(Condition.visible); // ���������, �������� �� ������� �� ���������� �������� (� ������ "���� �����")
    }
}
