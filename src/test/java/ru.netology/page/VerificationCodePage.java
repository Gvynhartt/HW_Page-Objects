package ru.netology.page;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import ru.netology.data.MrDataHelper;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class VerificationCodePage {
    private String codeXpath = "//span[@data-test-id='code']/descendant::input[@name='code']";
    private String continueXpath = "//button[@data-test-id='action-verify']/span[@class='button__content']";

    public DashboardPage enterVerificationCode() {
        $x(codeXpath).setValue(MrDataHelper.generateDefaultUser().getUserCode());
        $x(continueXpath).click();
        sleep(5000);
        String actualUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        String expectedUrl = "http://localhost:9999/dashboard";
        Assertions.assertEquals(expectedUrl, actualUrl);
        return new DashboardPage();
    }
}
