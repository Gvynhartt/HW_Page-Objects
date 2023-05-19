package ru.netology.data;

import lombok.Value;

public class MrDataHelper {
    private MrDataHelper() {}

    @Value
    public static class NewUserEntry {
        private String userLogin;
        private String userPassword;
    }

    public static NewUserEntry generateDefaultUser() {
        NewUserEntry defaultUser = new NewUserEntry("vasya", "qwerty123");
        return defaultUser;
    }

    public static NewUserEntry generateOtherUser(NewUserEntry theVasyaOriginal){
        return new NewUserEntry("Vasilij_Ivanovich", "jquken228");
    }

    @Value
    public static class VerificationCode {
        private String userCode;
    }

    public static VerificationCode generateVerificationCode(NewUserEntry userEntry) {
        return new VerificationCode("12345");
    }

    @Value
    public static class UserCard {
        private String cardNumber;
        private int defaultBalance;
    }

    public static UserCard generateCardNmb1(NewUserEntry userEntry) {
        return new UserCard("5559 0000 0000 0001", 10000);
    }

    public static UserCard generateCardNmb2(NewUserEntry userEntry) {
        return new UserCard("5559 0000 0000 0002", 10000);
    }
}
