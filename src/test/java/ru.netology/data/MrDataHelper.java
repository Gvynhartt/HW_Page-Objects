package ru.netology.data;

import lombok.Value;

public class MrDataHelper {

    @Value
    public static class NewUserEntry {
        private String userLogin;
        private String userPassword;
        private String userCode;
        private String userCardFirstNumber;
        private int userCardFirstBalance;
        private String userCardSecondNumber;
        private int userCardSecondBalance;
    }

    public static NewUserEntry generateDefaultUser() {
        NewUserEntry defaultUser = new NewUserEntry(
                "vasya",
                "qwerty123",
                "12345",
                "5559 0000 0000 0001",
                10000,
                "5559 0000 0000 0002",
                10000);
        return defaultUser;
    }
}
