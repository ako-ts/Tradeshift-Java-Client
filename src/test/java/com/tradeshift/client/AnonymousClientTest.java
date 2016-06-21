package com.tradeshift.client;

import com.tradeshift.api.InfoOps;
import org.junit.Test;

import java.util.UUID;

public class AnonymousClientTest {

    @Test
    public void testCall() throws Exception {
        TradeshiftRestClient restClient = TradeshiftRestClient.sandbox(new JerseyClientFactory(), "test java client");
        UUID clusterId = InfoOps.on(restClient).getClusterId();

        System.out.println("clusterId = " + clusterId);
    }
}