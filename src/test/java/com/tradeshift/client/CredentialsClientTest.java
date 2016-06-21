package com.tradeshift.client;

import com.tradeshift.api.AccountOps;
import org.junit.Test;

import java.util.HashMap;

public class CredentialsClientTest {

    @Test
    public void testCall() throws Exception {

        // client access credentials should be obtained in UI
        String clientId = "";
        String clientPassword = "";
        TradeshiftRestClient restClient = TradeshiftRestClient.sandbox(new JerseyClientFactory(), "test java client")
                .withClientCredentialsGrant(clientId, clientPassword);
        HashMap info = AccountOps.on(restClient).getInfo();

        System.out.println(info);
    }
}