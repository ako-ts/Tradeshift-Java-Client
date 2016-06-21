package com.tradeshift.client;

import com.tradeshift.api.AccountOps;
import org.junit.Test;

import java.util.HashMap;

public class PasswordClientTest {

    @Test
    public void testCall() throws Exception {

        // login credentials are same as used for UI login
        String login = "user@email.com";
        String password = "00000000";

        TradeshiftRestClient restClient = TradeshiftRestClient
                .sandbox(new JerseyClientFactory(), "test java client")
                .withPasswordGrant(login, password); // Mobile API Access application must be already activated

        HashMap info = AccountOps.on(restClient).getInfo();
        System.out.println(info);
    }
}