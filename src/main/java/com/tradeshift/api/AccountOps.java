package com.tradeshift.api;

import com.tradeshift.client.TradeshiftRestClient;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * API operations that are about the current account (user external/account)
 */
public class AccountOps {
    private final TradeshiftRestClient client;

    protected AccountOps(TradeshiftRestClient client) {
        this.client = client;
    }

    public static AccountOps on(TradeshiftRestClient client) {
        return new AccountOps(client);
    }

    /**
     * Gets information about the currently accessed account (external/account/info)
     */
    public HashMap getInfo() {
        return client.target().path("rest/external/account/info")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(HashMap.class);
    }
}
