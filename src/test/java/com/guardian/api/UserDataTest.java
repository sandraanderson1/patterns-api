package com.guardian.api;

import com.guardian.api.dataModels.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDataTest {

    @Test
    public void createsUserDataWithUserTierOnly() {
        UserData userData = new UserData.Builder("developer").build();
        assertEquals(userData.getUserName(), null);
    }

    @Test
    public void createsUserDataWithUserTierAndName() {
        UserData userData = new UserData.Builder("developer").withUserName("Bob").build();
        assertEquals(userData.getUserTier(), "developer");
        assertEquals(userData.getUserName(), "Bob");
    }

}